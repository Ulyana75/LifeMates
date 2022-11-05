package ru.ulyanaab.lifemates.ui.common.navigation.chats

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import ru.ulyanaab.lifemates.ui.chats.ChatsScreen
import ru.ulyanaab.lifemates.ui.chats.ChatsViewModel
import ru.ulyanaab.lifemates.ui.match.MatchScreen
import ru.ulyanaab.lifemates.ui.match.MatchViewModel

@ExperimentalPagerApi
@Composable
fun ChatsNavGraph(
    navController: NavHostController,
    matchViewModel: MatchViewModel,
    chatsViewModel: ChatsViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = ChatsNavItem.Chats.screenRoute
    ) {
        composable(ChatsNavItem.Chats.screenRoute) {
            ChatsScreen(navController = navController, chatsViewModel = chatsViewModel)
        }

        composable(ChatsNavItem.Matches.screenRoute) {
            MatchScreen(matchViewModel = matchViewModel)
        }
    }
}
