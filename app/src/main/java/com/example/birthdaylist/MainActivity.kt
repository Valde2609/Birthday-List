package com.example.birthdaylist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.birthdaylist.screens.AddFriendScreen
import com.example.birthdaylist.screens.EditFriendScreen
import com.example.birthdaylist.screens.HomeScreen
import com.example.birthdaylist.screens.LoginScreen
import com.example.birthdaylist.ui.theme.BirthdayListTheme
import com.example.birthdaylist.viewModel.AuthenticationViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BirthdayListTheme {
                val navController = rememberNavController()
                val authViewModel: AuthenticationViewModel = koinViewModel()
                
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.Login.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(NavRoutes.Login.route) {
                            LoginScreen(
                                user = authViewModel.user,
                                message = authViewModel.message,
                                signIn = { email, password -> authViewModel.signIn(email, password) },
                                register = { email, password -> authViewModel.register(email, password) },
                                navigateToNextScreen = {
                                    navController.navigate(NavRoutes.Home.route) {
                                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(NavRoutes.Home.route) {
                            HomeScreen(
                                onLogoutClick = {
                                    authViewModel.signOut()
                                    navController.navigate(NavRoutes.Login.route) {
                                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                                    }
                                },
                                onAddFriendClick = {
                                    navController.navigate(NavRoutes.AddFriend.route)
                                },
                                onEditFriendClick = { friend ->
                                    navController.navigate("${NavRoutes.EditFriend.route}/${friend.id}")
                                }
                            )
                        }

                        composable(NavRoutes.AddFriend.route) {
                            AddFriendScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onLogoutClick = {
                                    authViewModel.signOut()
                                    navController.navigate(NavRoutes.Login.route) {
                                        popUpTo(0)
                                    }
                                }
                            )
                        }

                        composable(
                            route = "${NavRoutes.EditFriend.route}/{friendId}",
                            arguments = listOf(navArgument("friendId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val friendId = backStackEntry.arguments?.getInt("friendId") ?: -1
                            EditFriendScreen(
                                friendId = friendId,
                                onNavigateBack = { navController.popBackStack() },
                                onLogoutClick = {
                                    authViewModel.signOut()
                                    navController.navigate(NavRoutes.Login.route) {
                                        popUpTo(0)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
