package com.example.birthdaylist.data

interface FriendsRepository {
    suspend fun getFriends(): NetworkResult<List<Friend>>
    suspend fun addFriend(friend: Friend): NetworkResult<Friend>
    suspend fun deleteFriend(friendId: Int): NetworkResult<Friend>
    suspend fun updateFriend(friendId: Int, friend: Friend): NetworkResult<Friend>
}