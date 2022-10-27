package ru.ulyanaab.lifemates.ui.common.navigation.general

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthState
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthStateHolder
import ru.ulyanaab.lifemates.ui.auth.AuthViewModel
import ru.ulyanaab.lifemates.ui.common.navigation.auth.AuthNavGraph
import ru.ulyanaab.lifemates.ui.feed.FeedViewModel
import ru.ulyanaab.lifemates.ui.loading.LoadingScreen
import ru.ulyanaab.lifemates.ui.loading.LoadingViewModel
import ru.ulyanaab.lifemates.ui.main.MainScreen
import ru.ulyanaab.lifemates.ui.profile.ProfileViewModel
import ru.ulyanaab.lifemates.ui.register.RegisterViewModel

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
                val bottomNavController = rememberNavController()

                MainScreen(
                    navController = bottomNavController,
                    profileViewModel = profileViewModel,
                    feedViewModel = feedViewModel,
                )

            } else {
                val authNavController = rememberNavController()

                AuthNavGraph(
                    navController = authNavController,
                    authViewModel = authViewModel,
                    registerViewModel = registerViewModel,
                )
            }
        }
    }
}
