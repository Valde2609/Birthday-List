package com.example.birthdaylist.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.birthdaylist.FriendsUIState
import com.example.birthdaylist.SingleFriendUIState
import com.example.birthdaylist.data.Friend
import com.example.birthdaylist.data.FriendsRepository
import com.example.birthdaylist.data.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FriendViewModel(
    private val friendsRepository: FriendsRepository
) : ViewModel() {
    private val _friendsUIState = MutableStateFlow(FriendsUIState())
    val friendsUIState: StateFlow<FriendsUIState> = _friendsUIState

    private val _singleFriendUIState = MutableStateFlow(SingleFriendUIState())
    val singleFriendUIState: StateFlow<SingleFriendUIState> = _singleFriendUIState

    private var originalFriendList: List<Friend> = emptyList()

    fun getFriends(userId: String?) {
        _friendsUIState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            when (val result = friendsRepository.getFriends()) {
                is NetworkResult.Success -> {
                    originalFriendList = result.data
                    _friendsUIState.update { uiState ->
                        uiState.copy(
                            isLoading = false,
                            friends = result.data.filter { it.userId == userId }
                        )
                    }
                }

                is NetworkResult.Error -> {
                    _friendsUIState.update {
                        it.copy(isLoading = false, error = result.error)
                    }
                }
            }
        }
    }

    fun addFriend(friend: Friend, userId: String?) {
        _singleFriendUIState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val friendWithUser = friend.copy(userId = userId)
            when (val result = friendsRepository.addFriend(friendWithUser)) {
                is NetworkResult.Success -> {
                    _singleFriendUIState.update {
                        it.copy(isLoading = false, friend = result.data)
                    }
                    getFriends(userId)
                }

                is NetworkResult.Error -> {
                    _singleFriendUIState.update {
                        it.copy(isLoading = false, error = result.error)
                    }
                }
            }
        }
    }

    fun deleteFriend(friendId: Int, userId: String?) {
        _singleFriendUIState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = friendsRepository.deleteFriend(friendId)) {
                is NetworkResult.Success -> {
                    _singleFriendUIState.update {
                        it.copy(isLoading = false, friend = result.data)
                    }
                    getFriends(userId)
                }

                is NetworkResult.Error -> {
                    _singleFriendUIState.update {
                        it.copy(isLoading = false, error = result.error)
                    }
                }
            }
        }
    }

    fun updateFriend(friendId: Int, friend: Friend, userId: String?) {
        _singleFriendUIState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = friendsRepository.updateFriend(friendId, friend)) {
                is NetworkResult.Success -> {
                    _singleFriendUIState.update {
                        it.copy(isLoading = false, friend = result.data)
                    }
                    getFriends(userId)
                }

                is NetworkResult.Error -> {
                    _singleFriendUIState.update {
                        it.copy(isLoading = false, error = result.error)
                    }
                }
            }
        }
    }

    fun filterByName(nameFragment: String, userId: String?) {
        if (nameFragment.isBlank()) {
            _friendsUIState.update {
                it.copy(friends = originalFriendList.filter { friend -> friend.userId == userId })
            }
        } else {
            _friendsUIState.update {
                it.copy(
                    friends = originalFriendList.filter { friend -> 
                        friend.userId == userId && friend.name.contains(nameFragment, ignoreCase = true) 
                    }
                )
            }
        }
    }

    fun sortByName(ascending: Boolean) {
        _friendsUIState.update {
            it.copy(friends = if (ascending) it.friends.sortedBy { friend -> friend.name }
            else it.friends.sortedByDescending { friend -> friend.name })
        }
    }
}
