package com.example.birthdaylist.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.example.birthdaylist.components.BirthdayTopAppBar
import com.example.birthdaylist.data.Friend
import com.example.birthdaylist.viewModel.AuthenticationViewModel
import com.example.birthdaylist.viewModel.FriendViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFriendScreen(
    friendId: Int,
    onLogoutClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    viewModel: FriendViewModel = koinViewModel(),
    authViewModel: AuthenticationViewModel = koinViewModel()
) {
    var name by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val userId = authViewModel.user?.uid

    val uiState by viewModel.friendsUIState.collectAsState()
    val friend = uiState.friends.find { it.id == friendId }

    LaunchedEffect(friend) {
        friend?.let {
            name = it.name
            if (it.birthDayOfMonth != null && it.birthMonth != null && it.birthYear != null) {
                val cal = Calendar.getInstance()
                cal.set(it.birthYear, it.birthMonth - 1, it.birthDayOfMonth)
                birthday = formatter.format(cal.time)
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        birthday = formatter.format(Date(it))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            BirthdayTopAppBar(
                title = "Edit Friend",
                onLogoutClick = onLogoutClick
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Name",
                    style = MaterialTheme.typography.titleMedium
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter name") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Birthday",
                    style = MaterialTheme.typography.titleMedium
                )
                OutlinedTextField(
                    value = birthday,
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                    placeholder = { Text("DD/MM/YYYY") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Select Date",
                            modifier = Modifier.clickable { showDatePicker = true }
                        )
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            if (name.isNotBlank() && birthday.isNotBlank() && friend != null) {
                                val date = formatter.parse(birthday)
                                val cal = Calendar.getInstance()
                                if (date != null) {
                                    cal.time = date
                                    viewModel.updateFriend(
                                        friend.id,
                                        friend.copy(
                                            name = name,
                                            birthDayOfMonth = cal.get(Calendar.DAY_OF_MONTH),
                                            birthMonth = cal.get(Calendar.MONTH) + 1,
                                            birthYear = cal.get(Calendar.YEAR)
                                        ),
                                        userId = userId
                                    )
                                    onNavigateBack()
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Update")
                    }
                }
            }
        }
    }
}
