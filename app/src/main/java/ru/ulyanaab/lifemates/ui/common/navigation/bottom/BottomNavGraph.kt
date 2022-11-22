package ru.ulyanaab.lifemates.ui.common.navigation.bottom

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    chatsNavController: NavHostController,
    profileViewModel: ProfileViewModel,
    feedViewModel: FeedViewModel,
    chatsViewModel: ChatsViewModel,
) {
    NavHost(
        navController = bottomNavController,
        startDestination = BottomNavItem.Feed.screenRoute
    ) {
        composable(BottomNavItem.Profile.screenRoute) {
            ProfileScreen(profileViewModel = profileViewModel)
        }
        composable(BottomNavItem.Feed.screenRoute) {
            FeedScreen(feedViewModel = feedViewModel)
        }
        composable(BottomNavItem.Chats.screenRoute) {
            ChatsScreen(
                navController = chatsNavController,
                chatsViewModel = chatsViewModel
            )
        }
    }
}
