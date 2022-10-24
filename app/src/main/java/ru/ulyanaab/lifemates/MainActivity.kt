package ru.ulyanaab.lifemates

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthStateHolder
import ru.ulyanaab.lifemates.ui.auth.AuthScreen
import ru.ulyanaab.lifemates.ui.auth.AuthViewModel
import ru.ulyanaab.lifemates.ui.register.RegisterFirstStage
import ru.ulyanaab.lifemates.ui.register.RegisterSecondStage
import ru.ulyanaab.lifemates.ui.register.RegisterViewModel
import ru.ulyanaab.lifemates.ui.theme.LifeMatesTheme
import javax.inject.Inject

@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authStateHolder: AuthStateHolder

    @Inject
    lateinit var authViewModel: AuthViewModel

    @Inject
    lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)

        super.onCreate(savedInstanceState)

        setContent {
            LifeMatesTheme {
                val navController = rememberNavController()
                val authEvent = authViewModel.authEventsFlow.collectAsState()

                NavHost(
                    navController = navController,
                    startDestination = "auth",
                ) {
                    composable("auth") {
                        AuthScreen(
                            authViewModel = authViewModel,
                            navController = navController,
                            authEvent = authEvent.value
                        )
                    }
                    composable("register_first_stage") {
                        RegisterFirstStage(
                            registerViewModel = registerViewModel,
                            navController = navController
                        )
                    }
                    composable("register_second_stage") {
                        RegisterSecondStage(
                            registerViewModel = registerViewModel,
                            navController = navController,
                            authEvent = authEvent.value
                        )
                    }
                    composable("feed") {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(Color.Green))
                    }
                }
            }
        }
    }
}
