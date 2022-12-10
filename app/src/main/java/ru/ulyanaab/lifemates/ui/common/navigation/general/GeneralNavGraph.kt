package ru.ulyanaab.lifemates.ui.common.navigation.general

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthState
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthStateHolder
import ru.ulyanaab.lifemates.ui.auth.AuthViewModel
import ru.ulyanaab.lifemates.ui.chats.ChatsViewModel
import ru.ulyanaab.lifemates.ui.common.navigation.auth.AuthNavGraph
import ru.ulyanaab.lifemates.ui.common.navigation.main.MainNavGraph
import ru.ulyanaab.lifemates.ui.feed.FeedViewModel
import ru.ulyanaab.lifemates.ui.interests.InterestsViewModel
import ru.ulyanaab.lifemates.ui.loading.LoadingScreen
import ru.ulyanaab.lifemates.ui.loading.LoadingViewModel
import ru.ulyanaab.lifemates.ui.match.MatchViewModel
import ru.ulyanaab.lifemates.ui.profile.ProfileViewModel
import ru.ulyanaab.lifemates.ui.register.RegisterViewModel
import ru.ulyanaab.lifemates.ui.single_chat.di.SingleChatDependencies

@ExperimentalPagerApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun GeneralNavGraph(
    navController: NavHostController,
    feedViewModel: FeedViewModel,
    profileViewModel: ProfileViewModel,
    authViewModel: AuthViewModel,
    registerViewModel: RegisterViewModel,
    loadingViewModel: LoadingViewModel,
    authStateHolder: AuthStateHolder,
    matchViewModel: MatchViewModel,
    chatsViewModel: ChatsViewModel,
    interestsViewModel: InterestsViewModel,
    singleChatDependencies: SingleChatDependencies,
) {
    val authState by authStateHolder.authStateFlow.collectAsState()

    NavHost(
        navController = navController,
        startDestination = GeneralNavItem.Loading.screenRoute
    ) {
        composable(GeneralNavItem.Loading.screenRoute) {
            LoadingScreen(
                loadingViewModel = loadingViewModel,
                navController = navController
            )
        }
        composable(GeneralNavItem.MainOrAuth.screenRoute) {
            if (authState == AuthState.AUTHORIZED) {
                val mainNavController = rememberNavController()

                MainNavGraph(
                    navController = mainNavController,
                    profileViewModel = profileViewModel,
                    feedViewModel = feedViewModel,
                    matchViewModel = matchViewModel,
                    chatsViewModel = chatsViewModel,
                    interestsViewModel = interestsViewModel,
                    singleChatDependencies = singleChatDependencies,
                )

            } else {
                val authNavController = rememberNavController()

                AuthNavGraph(
                    navController = authNavController,
                    authViewModel = authViewModel,
                    registerViewModel = registerViewModel,
                    interestsViewModel = interestsViewModel,
                )
            }
        }
    }
}
