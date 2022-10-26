package ru.ulyanaab.lifemates

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthStateHolder
import ru.ulyanaab.lifemates.ui.auth.AuthScreen
import ru.ulyanaab.lifemates.ui.auth.AuthViewModel
import ru.ulyanaab.lifemates.ui.common.navigation.BottomNavGraph
import ru.ulyanaab.lifemates.ui.common.navigation.BottomNavigation
import ru.ulyanaab.lifemates.ui.common.theme.LifeMatesTheme
import ru.ulyanaab.lifemates.ui.feed.FeedViewModel
import ru.ulyanaab.lifemates.ui.profile.ProfileViewModel
import ru.ulyanaab.lifemates.ui.register.RegisterFirstStage
import ru.ulyanaab.lifemates.ui.register.RegisterSecondStage
import ru.ulyanaab.lifemates.ui.register.RegisterViewModel
import javax.inject.Inject

@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authStateHolder: AuthStateHolder

    @Inject
    lateinit var authViewModel: AuthViewModel

    @Inject
    lateinit var registerViewModel: RegisterViewModel

    @Inject
    lateinit var profileViewModel: ProfileViewModel

    @Inject
    lateinit var feedViewModel: FeedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)

        super.onCreate(savedInstanceState)

        setContent {
            LifeMatesTheme {
                val navController1 = rememberNavController()
                val authEvent = authStateHolder.authStateFlow.collectAsState()

                NavHost(
                    navController = navController1,
                    startDestination = "auth",
                ) {
                    composable("auth") {
                        AuthScreen(
                            authViewModel = authViewModel,
                            navController = navController1,
                            authEvent = authEvent.value
                        )
                    }
                    composable("register_first_stage") {
                        RegisterFirstStage(
                            registerViewModel = registerViewModel,
                            navController = navController1
                        )
                    }
                    composable("register_second_stage") {
                        RegisterSecondStage(
                            registerViewModel = registerViewModel,
                            navController = navController1,
                            authEvent = authEvent.value
                        )
                    }
                    composable("main") {
                        val navController2 = rememberNavController()
                        Scaffold(
                            bottomBar = {
                                BottomNavigation(navController = navController2)
                            }
                        ) { innerPadding ->
                            Box(modifier = Modifier.padding(innerPadding)) {
                                BottomNavGraph(
                                    navController = navController2,
                                    profileViewModel = profileViewModel,
                                    feedViewModel = feedViewModel,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
