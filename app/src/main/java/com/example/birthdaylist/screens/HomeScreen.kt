package com.example.birthdaylist.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.example.birthdaylist.components.BirthdayTopAppBar
import com.example.birthdaylist.data.Friend
import com.example.birthdaylist.viewModel.AuthenticationViewModel
import com.example.birthdaylist.viewModel.FriendViewModel
import com.example.birthdaylist.viewModel.SortType

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: FriendViewModel = koinViewModel(),
    authViewModel: AuthenticationViewModel = koinViewModel(),
    onLogoutClick: () -> Unit = {},
    onAddFriendClick: () -> Unit = {},
    onEditFriendClick: (Friend) -> Unit = {}
) {
    val uiState by viewModel.friendsUIState.collectAsState()
    val userId = authViewModel.user?.uid
    var showSortMenu by remember { mutableStateOf(false) }
    var showFilterBar by remember { mutableStateOf(false) }
    
    var nameFilter by remember { mutableStateOf("") }
    var ageFilter by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        viewModel.getFriends(userId)
    }

    LaunchedEffect(nameFilter, ageFilter, userId) {
        val age = ageFilter.toIntOrNull()
        viewModel.filterFriends(nameFilter, age, userId)
    }

    Scaffold(
        topBar = {
            BirthdayTopAppBar(
                title = "Home",
                onLogoutClick = onLogoutClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddFriendClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Friend"
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { showFilterBar = !showFilterBar }) {
                    Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filter")
                }
                
                Box {
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort")
                    }
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sort by Name") },
                            onClick = {
                                viewModel.sortFriends(SortType.NAME)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Sort by Age") },
                            onClick = {
                                viewModel.sortFriends(SortType.AGE)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Sort by Birthday") },
                            onClick = {
                                viewModel.sortFriends(SortType.BIRTHDAY)
                                showSortMenu = false
                            }
                        )
                    }
                }
            }

            if (showFilterBar) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = nameFilter,
                        onValueChange = { nameFilter = it },
                        label = { Text("Name") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = ageFilter,
                        onValueChange = { ageFilter = it },
                        label = { Text("Age") },
                        modifier = Modifier.width(80.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (uiState.error != null) {
                    Text(
                        text = uiState.error ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(uiState.friends) { friend ->
                            FriendList(
                                friend = friend,
                                onDeleteClick = { viewModel.deleteFriend(friend.id, userId) },
                                onEditClick = { onEditFriendClick(friend) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FriendList(
    friend: Friend,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = friend.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Age: ${friend.age ?: "Unknown"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Birthday: ${friend.birthDayOfMonth}.${friend.birthMonth}.${friend.birthYear}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Friend"
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Friend"
                )
            }
        }
    }
}
