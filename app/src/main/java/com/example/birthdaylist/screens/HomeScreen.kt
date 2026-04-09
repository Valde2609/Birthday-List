package com.example.birthdaylist.screens

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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.birthdaylist.components.BirthdayTopAppBar
import com.example.birthdaylist.viewModel.Friend
import com.example.birthdaylist.viewModel.FriendsViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: FriendsViewModel = viewModel(),
    onLogoutClick: () -> Unit = {},
    onAddFriendClick: () -> Unit = {},
    onEditFriendClick: (Friend) -> Unit = {}
) {
    val friends = viewModel.friends

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
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(friends) { friend ->
                FriendList(
                    friend = friend,
                    onDeleteClick = { viewModel.deleteFriend(friend) },
                    onEditClick = { onEditFriendClick(friend) }
                )
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
                    text = "Age: ${friend.age}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Birthday: ${friend.birthday}",
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
