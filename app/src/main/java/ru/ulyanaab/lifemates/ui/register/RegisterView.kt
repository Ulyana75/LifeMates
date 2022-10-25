package ru.ulyanaab.lifemates.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthEvent
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel
import ru.ulyanaab.lifemates.ui.common.utils.showToast
import ru.ulyanaab.lifemates.ui.common.widget.ContactsBlock
import ru.ulyanaab.lifemates.ui.common.widget.DescriptionBlock
import ru.ulyanaab.lifemates.ui.common.widget.GenderChoiceBlock
import ru.ulyanaab.lifemates.ui.common.widget.PersonalUserInfo
import ru.ulyanaab.lifemates.ui.common.widget.ShowingGenderChoiceBlock
import ru.ulyanaab.lifemates.ui.common.widget.SimpleSpacer
import ru.ulyanaab.lifemates.ui.common.widget.TopBar
import ru.ulyanaab.lifemates.ui.common.widget.UserInfoButton
import ru.ulyanaab.lifemates.ui.common.widget.UserInfoPhotoBlock
import ru.ulyanaab.lifemates.ui.common.widget.UserInfoTitleWithInputs
import ru.ulyanaab.lifemates.ui.common.widget.validateInputs


@Composable
fun RegisterFirstStage(
    registerViewModel: RegisterViewModel,
    navController: NavController
) {
    // TODO validate password
    var login by remember { mutableStateOf(registerViewModel.getLoginAndPassword().first) }
    var password by remember { mutableStateOf(registerViewModel.getLoginAndPassword().second) }
    var passwordRepeat by remember { mutableStateOf(registerViewModel.getLoginAndPassword().second) }

    var isLoginError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }
    var isPasswordRepeatError by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.White),
        verticalArrangement = Arrangement.Top
    ) {
        RegisterTopBar {
            navController.popBackStack()
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
        ) {
            SimpleSpacer(height = 30.dp)
            UserInfoTitleWithInputs(
                inputsCount = 3,
                title = "Введите вашу почту и пароль",
                valueList = listOf(login, password, passwordRepeat),
                onValueChangeList = listOf(
                    {
                        login = it
                        isLoginError = false
                    },
                    {
                        password = it
                        isPasswordError = false
                    },
                    {
                        passwordRepeat = it
                        isPasswordRepeatError = false
                    }
                ),
                onValueClearList = listOf(
                    { login = "" },
                    { password = "" },
                    { passwordRepeat = "" }
                ),
                hintList = listOf("Почта", "Придумайте пароль", "Повторите пароль"),
                isErrorList = listOf(isLoginError, isPasswordError, isPasswordRepeatError),
                isPasswordList = listOf(false, true, true)
            )
            val context = LocalContext.current
            UserInfoButton(text = "Далее") {
                if (login.isEmpty()) {
                    isLoginError = true
                }
                if (password.isEmpty()) {
                    isPasswordError = true
                }
                if (passwordRepeat.isEmpty()) {
                    isPasswordRepeatError = true
                }
                if (login.isNotEmpty() && password.isNotEmpty() && passwordRepeat.isNotEmpty()) {
                    if (password != passwordRepeat) {
                        isPasswordError = true
                        isPasswordRepeatError = true
                        showToast(text = "Пароли не совпадают", context)
                    } else {
                        registerViewModel.saveLoginAndPassword(login, password)
                        navController.navigate("register_second_stage")
                    }
                }
            }
        }
    }
}

@Composable
fun RegisterSecondStage(
    registerViewModel: RegisterViewModel,
    navController: NavController,
    authEvent: AuthEvent
) {
    // TODO
    var wasNavigated by remember {
        mutableStateOf(false)
    }
    if (authEvent == AuthEvent.AUTHORIZATION_SUCCESS && !wasNavigated) {
        wasNavigated = true
        navController.navigate("feed")
    }

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var telegram by remember { mutableStateOf("") }
    var vk by remember { mutableStateOf("") }
    var viber by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var instagram by remember { mutableStateOf("") }

    var isNameError by remember { mutableStateOf(false) }
    var isContactsError by remember { mutableStateOf(false) }
    var chosenGender by remember { mutableStateOf<RoundedBlockUiModel?>(null) }
    var chosenShowingGender by remember { mutableStateOf<RoundedBlockUiModel?>(null) }


    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        RegisterTopBar {
            navController.popBackStack()
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .verticalScroll(rememberScrollState())
                .weight(1f, fill = false),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserInfoPhotoBlock {
                // TODO
            }
            PersonalUserInfo(
                name = name,
                age = age,
                onNameChange = {
                    name = it
                    isNameError = false
                },
                onAgeChange = { age = it },
                onNameClear = { name = "" },
                onAgeClear = { age = "" },
                isNameError = isNameError
            )
            GenderChoiceBlock(
                elements = listOf(
                    RoundedBlockUiModel("Мужской", false),
                    RoundedBlockUiModel("Женский", false),
                    RoundedBlockUiModel("Не бинарный", false),
                ),
                onChoiceChanged = { chosenGender = it }
            )
            ShowingGenderChoiceBlock(
                elements = listOf(
                    RoundedBlockUiModel("Мужской", false),
                    RoundedBlockUiModel("Женский", false),
                    RoundedBlockUiModel("Не бинарный", false),
                ),
                onChoiceChanged = { chosenShowingGender = it }
            )
            DescriptionBlock(
                description = description,
                onDescriptionChange = { description = it },
                onDescriptionClear = { description = "" }
            )
            ContactsBlock(
                telegram = telegram,
                vk = vk,
                viber = viber,
                whatsapp = whatsapp,
                instagram = instagram,
                onTelegramChange = {
                    telegram = it
                    isContactsError = false
                },
                onVkChange = {
                    vk = it
                    isContactsError = false
                },
                onViberChange = {
                    viber = it
                    isContactsError = false
                },
                onWhatsappChange = {
                    whatsapp = it
                    isContactsError = false
                },
                onInstagramChange = {
                    instagram = it
                    isContactsError = false
                },
                onTelegramClear = { telegram = "" },
                onVkClear = { vk = "" },
                onViberClear = { viber = "" },
                onWhatsappClear = { whatsapp = "" },
                onInstagramClear = { instagram = "" },
                isContactsError = isContactsError
            )
            val context = LocalContext.current
            UserInfoButton(
                text = "Зарегистрироваться",
                paddingBottom = 43.dp,
                onClick = {
                    if (
                        validateInputs(
                            context = context,
                            name = name,
                            telegram = telegram,
                            vk = vk,
                            viber = viber,
                            whatsapp = whatsapp,
                            instagram = instagram,
                            chosenGender = chosenGender,
                            showingGender = chosenShowingGender,
                            onNameError = { isNameError = true },
                            onContactsError = { isContactsError = true }
                        )
                    ) {
                        registerViewModel.onRegisterClick(
                            name = name,
                            age = age,
                            gender = chosenGender,
                            showingGender = chosenShowingGender,
                            description = description,
                            telegram = telegram,
                            vk = vk,
                            viber = viber,
                            whatsapp = whatsapp,
                            instagram = instagram
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun RegisterTopBar(
    onBackClick: () -> Unit
) {
    TopBar(
        text = "Регистрация",
        leadIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier.clickable {
                    onBackClick.invoke()
                }
            )
        },
    )
}
