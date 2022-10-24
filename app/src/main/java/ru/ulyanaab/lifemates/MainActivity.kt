package ru.ulyanaab.lifemates

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthEvent
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthStateHolder
import ru.ulyanaab.lifemates.ui.auth.AuthScreen
import ru.ulyanaab.lifemates.ui.auth.AuthViewModel
import ru.ulyanaab.lifemates.ui.register.RegisterScreen
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

                NavHost(
                    navController = navController,
                    startDestination = "auth_controller",
                ) {
                    composable("auth_controller") {
                        AuthController(authViewModel = authViewModel, navController = navController)
                    }
                    composable("register") {
                        RegisterScreen(
                            registerViewModel = registerViewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AuthController(
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val authEvent = authViewModel.authEventsFlow.collectAsState()
    if (authEvent.value == AuthEvent.AUTHORIZATION_SUCCESS) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green)
        )
    } else {
        AuthScreen(authViewModel = authViewModel, navController = navController)
    }
}
