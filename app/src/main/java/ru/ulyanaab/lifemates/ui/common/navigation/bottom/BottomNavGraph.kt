package ru.ulyanaab.lifemates.ui.common.navigation.bottom

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import ru.ulyanaab.lifemates.ui.chats.ChatsViewModel
import ru.ulyanaab.lifemates.ui.common.navigation.chats.ChatsNavGraph
import ru.ulyanaab.lifemates.ui.feed.FeedScreen
import ru.ulyanaab.lifemates.ui.feed.FeedViewModel
import ru.ulyanaab.lifemates.ui.match.MatchViewModel
import ru.ulyanaab.lifemates.ui.profile.ProfileScreen
import ru.ulyanaab.lifemates.ui.profile.ProfileViewModel

@ExperimentalPagerApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
    feedViewModel: FeedViewModel,
    matchViewModel: MatchViewModel,
    chatsViewModel: ChatsViewModel,
) {
    val chatsNavController = rememberNavController()

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
            ChatsNavGraph(
                navController = chatsNavController,
                matchViewModel = matchViewModel,
                chatsViewModel = chatsViewModel,
            )
        }
    }
}
