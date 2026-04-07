package com.example.birthdaylist.viewModel


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class Friend(val id: Int, val name: String, val birthday: String)

class FriendsViewModel : ViewModel() {

    private val _friends = mutableStateListOf<Friend>()
    val friends: List<Friend> get() = _friends

    init {
        _friends.addAll(
            listOf(
                Friend(1,"Anna", "12.03.1998"),
                Friend(2, "Peter", "08.07.1997"),
                Friend(3, "Maria", "25.11.1999")
            )
        )
    }

    fun getFriendById(id: Int): Friend? {
        return _friends.find { it.id == id }
    }

    fun addFriend(friend: Friend) {
        _friends.add(friend)
    }

    fun deleteFriend(friend: Friend) {
        _friends.remove(friend)
    }

    fun updateFriend(oldFriend: Friend, newFriend: Friend) {
        val index = _friends.indexOf(oldFriend)
        if (index != -1) _friends[index] = newFriend
    }
}