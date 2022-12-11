package ru.ulyanaab.lifemates.ui.common.navigation.bottom

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import ru.ulyanaab.lifemates.ui.chats.ChatsScreen
import ru.ulyanaab.lifemates.ui.chats.ChatsViewModel
import ru.ulyanaab.lifemates.ui.feed.FeedScreen
import ru.ulyanaab.lifemates.ui.feed.FeedViewModel
import ru.ulyanaab.lifemates.ui.profile.ProfileScreen
import ru.ulyanaab.lifemates.ui.profile.ProfileViewModel

@ExperimentalPagerApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun BottomNavGraph(
    bottomNavController: NavHostController,
    mainNavController: NavHostController,
    profileViewModel: ProfileViewModel,
    feedViewModel: FeedViewModel,
    chatsViewModel: ChatsViewModel,
) {
    NavHost(
        navController = bottomNavController,
        startDestination = BottomNavItem.Feed.screenRoute
    ) {
        composable(BottomNavItem.Profile.screenRoute) {
            ProfileScreen(
                profileViewModel = profileViewModel,
                navController = mainNavController,
            )
        }
        composable(BottomNavItem.Feed.screenRoute) {
            FeedScreen(
                feedViewModel = feedViewModel,
                navController = mainNavController,
            )
        }
        composable(BottomNavItem.Chats.screenRoute) {
            ChatsScreen(
                navController = mainNavController,
                chatsViewModel = chatsViewModel
            )
        }
    }
}
