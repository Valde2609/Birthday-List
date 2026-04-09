package com.example.birthdaylist.viewModel


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

data class Friend(
    val id: Int,
    val name: String,
    val birthday: String,
    val age: Int = 0
)

class FriendsViewModel : ViewModel() {

    private val _friends = mutableStateListOf<Friend>()
    val friends: List<Friend> get() = _friends

    init {
        _friends.addAll(
            listOf(
                Friend(1, "Jesper", "25.06.1999", calculateAge("25.06.1999")),
                Friend(2, "Gustav", "26.07.1999", calculateAge("26.07.1999")),
                Friend(3, "Emil", "28.03.2000", calculateAge("28.03.2000"))
            )
        )
    }

    private fun calculateAge(birthday: String): Int {
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val birthDate = LocalDate.parse(birthday, formatter)
            Period.between(birthDate, LocalDate.now()).years
        } catch (e: Exception) {
            0
        }
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