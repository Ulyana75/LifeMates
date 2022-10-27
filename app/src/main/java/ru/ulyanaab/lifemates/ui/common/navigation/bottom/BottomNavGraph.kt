package ru.ulyanaab.lifemates.ui.common.navigation.bottom

import androidx.compose.animation.ExperimentalAnimationApi
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
import ru.ulyanaab.lifemates.ui.feed.FeedScreen
import ru.ulyanaab.lifemates.ui.feed.FeedViewModel
import ru.ulyanaab.lifemates.ui.profile.ProfileScreen
import ru.ulyanaab.lifemates.ui.profile.ProfileViewModel

@ExperimentalAnimationApi
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
    feedViewModel: FeedViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Feed.screenRoute
    ) {
        composable(BottomNavItem.Profile.screenRoute) {
            ProfileScreen(profileViewModel = profileViewModel)
        }
        composable(BottomNavItem.Feed.screenRoute) {
            FeedScreen(feedViewModel = feedViewModel)
        }
        composable(BottomNavItem.Chats.screenRoute) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Cyan)
            ) {
                Text(text = "chats")
            }
        }
    }
}
