package ru.ulyanaab.lifemates

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthEvent
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthStateHolder
import ru.ulyanaab.lifemates.ui.auth.AuthScreen
import ru.ulyanaab.lifemates.ui.auth.AuthViewModel
import ru.ulyanaab.lifemates.ui.theme.LifeMatesTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authStateHolder: AuthStateHolder

    @Inject
    lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)

        super.onCreate(savedInstanceState)

        setContent {
            LifeMatesTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val authEvent = authStateHolder.authStateFlow.collectAsState()
                    if (authEvent.value == AuthEvent.AUTHORIZATION_SUCCESS) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.Green)
                        )
                    } else {
                        AuthScreen(authViewModel = authViewModel)
                    }
                }
            }
        }
    }
}
