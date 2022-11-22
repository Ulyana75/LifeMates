package ru.ulyanaab.lifemates.ui.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import ru.ulyanaab.lifemates.ui.chats.ChatsViewModel
import ru.ulyanaab.lifemates.ui.common.navigation.bottom.BottomNavGraph
import ru.ulyanaab.lifemates.ui.common.navigation.bottom.BottomNavigation
import ru.ulyanaab.lifemates.ui.feed.FeedViewModel
import ru.ulyanaab.lifemates.ui.match.MatchViewModel
import ru.ulyanaab.lifemates.ui.profile.ProfileViewModel

@ExperimentalPagerApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun MainScreen(
    bottomNavController: NavHostController,
    chatsNavController: NavHostController,
    profileViewModel: ProfileViewModel,
    feedViewModel: FeedViewModel,
    chatsViewModel: ChatsViewModel,
) {
    Scaffold(
        bottomBar = {
            BottomNavigation(navController = bottomNavController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            BottomNavGraph(
                bottomNavController = bottomNavController,
                chatsNavController = chatsNavController,
                profileViewModel = profileViewModel,
                feedViewModel = feedViewModel,
                chatsViewModel = chatsViewModel,
            )
        }
    }
}
