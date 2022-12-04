package ru.ulyanaab.lifemates.ui.common.navigation.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.ulyanaab.lifemates.ui.auth.AuthScreen
import ru.ulyanaab.lifemates.ui.auth.AuthViewModel
import ru.ulyanaab.lifemates.ui.interests.InterestsScreen
import ru.ulyanaab.lifemates.ui.interests.InterestsViewModel
import ru.ulyanaab.lifemates.ui.register.RegisterFirstStage
import ru.ulyanaab.lifemates.ui.register.RegisterSecondStage
import ru.ulyanaab.lifemates.ui.register.RegisterViewModel

@Composable
fun AuthNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    registerViewModel: RegisterViewModel,
    interestsViewModel: InterestsViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = AuthNavItem.Auth.screenRoute
    ) {
        composable(AuthNavItem.Auth.screenRoute) {
            AuthScreen(
                authViewModel = authViewModel,
                navController = navController,
            )
        }
        composable(AuthNavItem.RegisterFirst.screenRoute) {
            RegisterFirstStage(
                registerViewModel = registerViewModel,
                navController = navController
            )
        }
        composable(AuthNavItem.RegisterSecond.screenRoute) {
            RegisterSecondStage(
                registerViewModel = registerViewModel,
                navController = navController,
            )
        }
        composable(AuthNavItem.Interests.screenRoute) {
            InterestsScreen(
                interestsViewModel = interestsViewModel,
                navController = navController
            )
        }
    }
}
