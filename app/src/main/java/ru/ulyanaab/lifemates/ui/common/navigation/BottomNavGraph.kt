package ru.ulyanaab.lifemates.ui.common.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.ulyanaab.lifemates.ui.profile.ProfileScreen
import ru.ulyanaab.lifemates.ui.profile.ProfileViewModel

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    profileViewModel: ProfileViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "feed"
    ) {
        composable(BottomNavItem.Profile.screenRoute) {
            ProfileScreen(profileViewModel = profileViewModel)
        }
        composable(BottomNavItem.Feed.screenRoute) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Blue)) {
                Text(text = "feed")
            }
        }
        composable(BottomNavItem.Chats.screenRoute) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Cyan)) {
                Text(text = "chats")
            }
        }
    }
}
