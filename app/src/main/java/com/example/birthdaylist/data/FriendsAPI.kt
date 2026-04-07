package com.example.birthdaylist.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FriendsAPI {
    @GET("persons")
    suspend fun getFriends(): Response<List<Friend>>

    @POST("persons")
    suspend fun addFriends(@Body friend: Friend): Response<Friend>

    @DELETE("persons/{Id}")
    suspend fun deleteBook(@Path("friendId") friendId: Int): Response<Friend>

    @PUT("persons/{Id}")
    suspend fun updateBook(@Path("friendId") friendId: Int, @Body friend: Friend): Response<Friend>
}