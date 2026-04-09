package com.example.birthdaylist.data

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FriendsRepositoryImplementation(
    private val friendsAPI: FriendsAPI,
    private val dispatcher: CoroutineDispatcher
) : FriendsRepository {
    override suspend fun getFriends(): NetworkResult<List<Friend>> {
        return withContext(dispatcher) {
            try {
                val response = friendsAPI.getFriends()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        NetworkResult.Success(body)
                    } else {
                        NetworkResult.Error("Response body is null")
                    }
                } else {
                    NetworkResult.Error(response.message())
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    override suspend fun addFriend(friend: Friend): NetworkResult<Friend> {
        return withContext(dispatcher) {
            try {
                val response = friendsAPI.addFriend(friend)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null)
                        NetworkResult.Success(body)
                    else
                        NetworkResult.Error("Response body is null")
                } else
                    NetworkResult.Error(response.message())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    override suspend fun deleteFriend(friendId: Int): NetworkResult<Friend> {
        return withContext(dispatcher) {
            try {
                val response = friendsAPI.deleteFriend(friendId)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null)
                        NetworkResult.Success(body)
                    else
                        NetworkResult.Error("Response body is null")
                } else
                    NetworkResult.Error(response.message())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    override suspend fun updateFriend(friendId: Int, friend: Friend): NetworkResult<Friend> {
        return withContext(dispatcher) {
            try {
                val response = friendsAPI.updateFriend(friendId, friend)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        NetworkResult.Success(body)
                    } else {
                        NetworkResult.Error("Response body is null")
                    }
                } else
                    NetworkResult.Error(response.message())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }
}