package com.example.birthdaylist.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.example.birthdaylist.components.BirthdayTopAppBar
import com.example.birthdaylist.data.Friend
import com.example.birthdaylist.viewModel.AuthenticationViewModel
import com.example.birthdaylist.viewModel.FriendViewModel

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

    LaunchedEffect(userId) {
        viewModel.getFriends(userId)
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
        Box(modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            
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
