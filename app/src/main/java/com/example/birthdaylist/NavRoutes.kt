package com.example.birthdaylist

sealed class NavRoutes(val route: String) {
    data object EditFriend : NavRoutes("edit_friend_screen")
    data object Home : NavRoutes("home_screen")
    data object Login : NavRoutes("login_screen")
    data object AddFriend : NavRoutes("create_new_friend")
}