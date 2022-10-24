package ru.ulyanaab.lifemates.ui.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthEvent
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel
import ru.ulyanaab.lifemates.ui.common.utils.showToast
import ru.ulyanaab.lifemates.ui.theme.GreyLight
import ru.ulyanaab.lifemates.ui.theme.Shapes
import ru.ulyanaab.lifemates.ui.theme.Typography
import ru.ulyanaab.lifemates.ui.widget.Button
import ru.ulyanaab.lifemates.ui.widget.EditText
import ru.ulyanaab.lifemates.ui.widget.RoundedBlocksSingleChoice
import ru.ulyanaab.lifemates.ui.widget.SimpleSpacer
import ru.ulyanaab.lifemates.ui.widget.TopBar


@Composable
fun RegisterFirstStage(
    registerViewModel: RegisterViewModel,
    navController: NavController
) {
    // TODO add minLength to password
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordRepeat by remember { mutableStateOf("") }

    var isLoginError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }
    var isPasswordRepeatError by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

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
            RegisterTitleWithInputs(
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
            RegisterButton(text = "Далее") {
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
                        focusManager.clearFocus()
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
    if (authEvent == AuthEvent.AUTHORIZATION_SUCCESS) {
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
            RegisterPhotoBlock {
                // TODO
            }
            RegisterTitleWithInputs(
                inputsCount = 2,
                title = "Личная информация",
                valueList = listOf(name, age),
                onValueChangeList = listOf(
                    {
                        name = it
                        isNameError = false
                    },
                    { age = it }
                ),
                onValueClearList = listOf(
                    { name = "" },
                    { age = "" }
                ),
                hintList = listOf("Имя", "Возраст"),
                isErrorList = listOf(isNameError)
            )
            GenderBlock(onChoiceChanged = { chosenGender = it })
            RegisterTitleWithInputs(
                inputsCount = 1,
                title = "О себе",
                valueList = listOf(description),
                onValueChangeList = listOf { description = it },
                onValueClearList = listOf { description = "" },
                hintList = listOf("О себе"),
            )
            RegisterTitleWithInputs(
                inputsCount = 5,
                title = "Ваши контакты",
                valueList = listOf(telegram, vk, viber, whatsapp, instagram),
                onValueChangeList = listOf(
                    {
                        telegram = it
                        isContactsError = false
                    },
                    {
                        vk = it
                        isContactsError = false
                    },
                    {
                        viber = it
                        isContactsError = false
                    },
                    {
                        whatsapp = it
                        isContactsError = false
                    },
                    {
                        instagram = it
                        isContactsError = false
                    },
                ),
                onValueClearList = listOf(
                    { telegram = "" },
                    { vk = "" },
                    { viber = "" },
                    { whatsapp = "" },
                    { instagram = "" },
                ),
                hintList = listOf("Telegram", "VK", "Viber", "Whatsapp", "Instagram"),
                isErrorList = listOf(
                    isContactsError,
                    isContactsError,
                    isContactsError,
                    isContactsError,
                    isContactsError
                )
            )
            val context = LocalContext.current
            RegisterButton(
                text = "Зарегистрироваться",
                paddingBottom = 43.dp,
                onClick = {
                    if (name.isEmpty()) {
                        isNameError = true
                        showToast("Имя не должно быть пустым", context)
                    }
                    if (telegram.isEmpty() && vk.isEmpty()
                        && viber.isEmpty() && whatsapp.isEmpty()
                        && instagram.isEmpty()
                    ) {
                        isContactsError = true
                        showToast("Заполните хотя бы один контакт", context)
                    }
                    if (chosenGender == null) {
                        showToast("Вы не указали пол", context)
                    }

                    if (name.isNotEmpty() &&
                        (telegram.isNotEmpty() || vk.isNotEmpty()
                                || viber.isNotEmpty() || whatsapp.isNotEmpty()
                                || instagram.isNotEmpty())
                        && chosenGender != null
                    ) {
                        registerViewModel.onRegisterClick(
                            name = name,
                            age = age,
                            gender = chosenGender,
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
fun RegisterTitleWithInputs(
    inputsCount: Int,
    title: String,
    valueList: List<String>,
    onValueChangeList: List<(String) -> Unit>,
    onValueClearList: List<() -> Unit>,
    hintList: List<String>,
    isErrorList: List<Boolean> = emptyList(),
    isPasswordList: List<Boolean> = emptyList()
) {
    RegisterBlockTitle(
        text = title,
        paddingBottom = 10.dp
    )
    for (i in 0 until inputsCount) {
        RegisterEditText(
            value = valueList[i],
            onValueChange = onValueChangeList[i],
            onValueClear = onValueClearList[i],
            hint = hintList[i],
            isError = isErrorList.getOrNull(i) ?: false,
            paddingBottom = if (i == inputsCount - 1) 45.dp else 10.dp,
            isPassword = isPasswordList.getOrNull(i) ?: false
        )
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

@Composable
fun RegisterBlockTitle(
    text: String,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp
) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingTop, bottom = paddingBottom),
        style = Typography.h2.copy(color = Color.Black),
        textAlign = TextAlign.Start
    )
}

@Composable
fun RegisterButton(
    text: String,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingTop, bottom = paddingBottom),
        text = text,
        isHighlighted = true,
        onClick = onClick
    )
}

@Composable
fun RegisterEditText(
    value: String,
    onValueChange: (String) -> Unit,
    onValueClear: () -> Unit,
    hint: String,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
    isError: Boolean = false,
    isPassword: Boolean = false
) {

    EditText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingTop, bottom = paddingBottom),
        hint = hint,
        isError = isError,
        value = value,
        onValueChange = onValueChange,
        onClearClicked = onValueClear,
        isPassword = isPassword
    )
}

@Composable
fun RegisterPhotoBlock(
    onUploadButtonClick: () -> Unit
) {
    RegisterBlockTitle(
        text = "Фото",
        paddingTop = 30.dp,
        paddingBottom = 10.dp
    )
    Box( // TODO use image
        modifier = Modifier
            .height(439.dp)
            .fillMaxWidth()
            .clip(Shapes.small)
            .background(color = GreyLight)
    )
    RegisterButton(
        text = "Загрузить",
        paddingTop = 20.dp,
        paddingBottom = 45.dp,
        onClick = onUploadButtonClick
    )
}

@Composable
fun GenderBlock(onChoiceChanged: (RoundedBlockUiModel) -> Unit) {
    RegisterBlockTitle(text = "Ваш пол")
    RoundedBlocksSingleChoice(
        onChoiceChanged = onChoiceChanged,
        elementsList = listOf(
            RoundedBlockUiModel("Мужской", false),
            RoundedBlockUiModel("Женский", false),
            RoundedBlockUiModel("Не бинарный", false),
        )
    )
}

@Preview
@Composable
fun RegisterSecondStagePreview() {
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


    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        RegisterTopBar {
            //navController.popBackStack()
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .verticalScroll(rememberScrollState())
                .weight(1f, fill = false),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegisterPhotoBlock {
                // TODO
            }
            RegisterTitleWithInputs(
                inputsCount = 2,
                title = "Личная информация",
                valueList = listOf(name, age),
                onValueChangeList = listOf(
                    {
                        name = it
                        isNameError = false
                    },
                    { age = it }
                ),
                onValueClearList = listOf(
                    { name = "" },
                    { age = "" }
                ),
                hintList = listOf("Имя", "Возраст"),
                isErrorList = listOf(isNameError)
            )
            GenderBlock(onChoiceChanged = { chosenGender = it })
            RegisterTitleWithInputs(
                inputsCount = 1,
                title = "О себе",
                valueList = listOf(description),
                onValueChangeList = listOf { description = it },
                onValueClearList = listOf { description = "" },
                hintList = listOf("О себе"),
            )
            RegisterTitleWithInputs(
                inputsCount = 2,
                title = "Ваши контакты",
                valueList = listOf(telegram, vk, viber, whatsapp, instagram),
                onValueChangeList = listOf(
                    {
                        telegram = it
                        isContactsError = false
                    },
                    {
                        vk = it
                        isContactsError = false
                    },
                    {
                        viber = it
                        isContactsError = false
                    },
                    {
                        whatsapp = it
                        isContactsError = false
                    },
                    {
                        instagram = it
                        isContactsError = false
                    },
                ),
                onValueClearList = listOf(
                    { name = "" },
                    { age = "" }
                ),
                hintList = listOf("Имя", "Возраст"),
                isErrorList = listOf(isNameError)
            )
            RegisterButton(
                text = "Зарегистрироваться",
                paddingBottom = 43.dp,
                onClick = {
                    //TODO
                }
            )
        }
    }
}

@Preview
@Composable
fun RegisterFirstStagePreview() {
    // TODO add minLength to password
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordRepeat by remember { mutableStateOf("") }

    var isPasswordsError by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.White),
        verticalArrangement = Arrangement.Top
    ) {
        RegisterTopBar {
            //navController.popBackStack()
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
        ) {
            SimpleSpacer(height = 30.dp)
            RegisterTitleWithInputs(
                inputsCount = 3,
                title = "Введите вашу почту и пароль",
                valueList = listOf(login, password, passwordRepeat),
                onValueChangeList = listOf(
                    { login = it },
                    {
                        password = it
                        isPasswordsError = false
                    },
                    {
                        passwordRepeat = it
                        isPasswordsError = false
                    }
                ),
                onValueClearList = listOf(
                    { login = "" },
                    { password = "" },
                    { passwordRepeat = "" }
                ),
                hintList = listOf("Почта", "Придумайте пароль", "Повторите пароль"),
                isErrorList = listOf(false, isPasswordsError, isPasswordsError)
            )
            val context = LocalContext.current
            RegisterButton(text = "Далее") {
                if (password != passwordRepeat) {
                    isPasswordsError = true
                    Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
