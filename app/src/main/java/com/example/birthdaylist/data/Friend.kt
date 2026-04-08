package com.example.birthdaylist.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable

data class Friend(
    val id: Int = -1,
    val userId: String? = null,
    val name: String,
    val birthYear: Int? = null,
    val birthMonth: Int? = null,
    val birthDayOfMonth: Int? = null,
    val remarks: String? = null,
    val pictureUrl: String? = null,
    val age: Int? = null
)

