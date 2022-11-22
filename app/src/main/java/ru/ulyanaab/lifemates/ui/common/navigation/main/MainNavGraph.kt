package ru.ulyanaab.lifemates.ui.common.navigation.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import ru.ulyanaab.lifemates.ui.chats.ChatsViewModel
import ru.ulyanaab.lifemates.ui.feed.FeedViewModel
import ru.ulyanaab.lifemates.ui.main.MainScreen
import ru.ulyanaab.lifemates.ui.match.MatchScreen
import ru.ulyanaab.lifemates.ui.match.MatchViewModel
import ru.ulyanaab.lifemates.ui.profile.ProfileViewModel

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalPagerApi
@Composable
fun MainNavGraph(
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
    feedViewModel: FeedViewModel,
    matchViewModel: MatchViewModel,
    chatsViewModel: ChatsViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = MainNavItem.MainScreen.screenRoute
    ) {
        composable(MainNavItem.MainScreen.screenRoute) {
            val bottomNavController = rememberNavController()

            MainScreen(
                bottomNavController = bottomNavController,
                chatsNavController = navController,
                profileViewModel = profileViewModel,
                feedViewModel = feedViewModel,
                chatsViewModel = chatsViewModel,
            )
        }

        composable(MainNavItem.Matches.screenRoute) {
            MatchScreen(
                matchViewModel = matchViewModel,
                navController = navController
            )
        }
    }
}
