package ru.ulyanaab.lifemates.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.ulyanaab.lifemates.ui.theme.Shapes
import ru.ulyanaab.lifemates.ui.theme.Typography
import ru.ulyanaab.lifemates.ui.widget.Button
import ru.ulyanaab.lifemates.ui.widget.EditText

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
) {
    Surface(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF3498FF),
                            Color(0xFFFF00D6)
                        ),
                    )
                )
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NameText(Modifier.padding(bottom = 67.dp))
            LoginForm(
                onLoginButtonClick = { login, password ->
                    authViewModel.onLoginClick(login, password)
                },
                onRegisterButtonClick = {
                    navController.navigate("register_first_stage")
                }
            )
        }
    }
}

@Composable
fun NameText(modifier: Modifier = Modifier) {
    Text(
        text = "LifeMates",
        modifier = modifier,
        style = Typography.h1.copy(color = Color.White)
    )
}

@Composable
fun LoginForm(
    onLoginButtonClick: (login: String, password: String) -> Unit,
    onRegisterButtonClick: () -> Unit,
) {

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isLoginError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }

    Surface(
        shape = Shapes.large,
        color = Color.White,
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditText(
                modifier = Modifier.fillMaxWidth(),
                hint = "Почта",
                isError = isLoginError,
                value = login,
                onValueChange = { value ->
                    login = value
                    isLoginError = false
                },
                onClearClicked = { login = "" }
            )
            EditText(
                modifier = Modifier.fillMaxWidth(),
                hint = "Пароль",
                isError = isPasswordError,
                value = password,
                onValueChange = { value ->
                    password = value
                    isPasswordError = false
                },
                onClearClicked = { password = "" },
                isPassword = true,
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (login.isNotEmpty() && password.isNotEmpty()) {
                        onLoginButtonClick.invoke(login, password)
                    } else {
                        if (login.isEmpty()) {
                            isLoginError = true
                        }
                        if (password.isEmpty()) {
                            isPasswordError = true
                        }
                    }
                },
                isHighlighted = true,
                text = "Войти"
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onRegisterButtonClick.invoke()
                },
                isHighlighted = false,
                text = "Зарегистрироваться"
            )
        }
    }
}

@Preview
@Composable
fun AuthPreview() {
    Surface(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF3498FF),
                            Color(0xFFFF00D6)
                        ),
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NameText(Modifier.padding(bottom = 67.dp))
            LoginForm(
                onLoginButtonClick = { login, password ->
                },
                onRegisterButtonClick = {
                    // TODO navigate
                }
            )
        }
    }
}
