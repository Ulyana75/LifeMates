package ru.ulyanaab.lifemates.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.ulyanaab.lifemates.domain.auth.model.LoginModel
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthEvent
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthEventType
import ru.ulyanaab.lifemates.ui.common.theme.Shapes
import ru.ulyanaab.lifemates.ui.common.theme.Typography
import ru.ulyanaab.lifemates.ui.common.widget.Button
import ru.ulyanaab.lifemates.ui.common.widget.EditText
import ru.ulyanaab.lifemates.ui.common.widget.InfoDialog
import ru.ulyanaab.lifemates.ui.common.widget.LoadingView

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    authEvent: AuthEvent
) {
    authViewModel.attach()
    val isLoading by authViewModel.isLoading.collectAsState()

    LaunchedEffect(authEvent) {
        if (authEvent.type == AuthEventType.AUTHORIZATION_SUCCESS) {
            navController.navigate("main")
        }
    }

    AuthDialogController(authEvent = authEvent)

    if (isLoading) {
        LoadingView()
    }
    AuthView(
        authViewModel = authViewModel,
        navController = navController
    )
}

@Composable
fun AuthView(
    authViewModel: AuthViewModel,
    navController: NavController
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
                savedLoginModel = authViewModel.savedLoginModel,
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
fun AuthDialogController(authEvent: AuthEvent) {
    val openDialog = remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf<String?>(null) }

    when (authEvent.type) {
        AuthEventType.AUTHORIZATION_SUCCESS, AuthEventType.UNAUTHORIZED -> {
            openDialog.value = false
        }
        AuthEventType.WRONG_PASSWORD -> {
            openDialog.value = true
            title = "Неверный логин или пароль"
        }
        else -> {
            openDialog.value = true
            title = "Произошла неизвестная ошибка"
            text = "Попробуйте еще раз"
        }
    }

    InfoDialog(
        openDialog = openDialog,
        title = title,
        text = text
    )
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
    savedLoginModel: LoginModel?,
    onLoginButtonClick: (login: String, password: String) -> Unit,
    onRegisterButtonClick: () -> Unit,
) {

    var login by remember { mutableStateOf(savedLoginModel?.email ?: "") }
    var password by remember { mutableStateOf(savedLoginModel?.password ?: "") }

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
                savedLoginModel = null,
                onLoginButtonClick = { _, _ ->
                },
                onRegisterButtonClick = {
                    // TODO navigate
                }
            )
        }
    }
}
