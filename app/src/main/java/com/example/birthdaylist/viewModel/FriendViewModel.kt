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
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

enum class SortType {
    NAME, AGE, BIRTHDAY
}

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

    fun filterFriends(nameFragment: String?, age: Int?, userId: String?) {
        _friendsUIState.update { uiState ->
            val filteredList = originalFriendList.filter { friend ->
                val matchesUser = friend.userId == userId
                val matchesName = nameFragment.isNullOrBlank() || friend.name.contains(nameFragment, ignoreCase = true)
                val matchesAge = age == null || friend.age == age
                matchesUser && matchesName && matchesAge
            }
            uiState.copy(friends = filteredList)
        }
    }

    fun filterByName(nameFragment: String, userId: String?) {
        filterFriends(nameFragment, null, userId)
    }

    fun sortFriends(sortType: SortType, ascending: Boolean = true) {
        _friendsUIState.update { uiState ->
            val sortedList = when (sortType) {
                SortType.NAME -> {
                    if (ascending) uiState.friends.sortedBy { it.name }
                    else uiState.friends.sortedByDescending { it.name }
                }
                SortType.AGE -> {
                    if (ascending) uiState.friends.sortedBy { it.age ?: 0 }
                    else uiState.friends.sortedByDescending { it.age ?: 0 }
                }
                SortType.BIRTHDAY -> {
                    if (ascending) uiState.friends.sortedWith(compareBy({ it.birthMonth ?: 0 }, { it.birthDayOfMonth ?: 0 }))
                    else uiState.friends.sortedWith(compareByDescending<Friend> { it.birthMonth ?: 0 }.thenByDescending { it.birthDayOfMonth ?: 0 })
                }
            }
            uiState.copy(friends = sortedList)
        }
    }

    fun sortByName(ascending: Boolean) {
        sortFriends(SortType.NAME, ascending)
    }

    fun calculateAge(birthday: String): Int {
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val birthDate = LocalDate.parse(birthday, formatter)
            Period.between(birthDate, LocalDate.now()).years
        } catch (e: Exception) {
            0
        }
    }
}
