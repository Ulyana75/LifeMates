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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import ru.ulyanaab.lifemates.domain.common.state_holders.RegisterEvent
import ru.ulyanaab.lifemates.domain.common.state_holders.RegisterEventType
import ru.ulyanaab.lifemates.ui.common.UploadPhotoViewModel
import ru.ulyanaab.lifemates.ui.common.navigation.auth.AuthNavItem
import ru.ulyanaab.lifemates.ui.common.utils.showToast
import ru.ulyanaab.lifemates.ui.common.widget.DescriptionBlock
import ru.ulyanaab.lifemates.ui.common.widget.GenderChoiceBlock
import ru.ulyanaab.lifemates.ui.common.widget.InfoDialog
import ru.ulyanaab.lifemates.ui.common.widget.InterestsChoiceBlock
import ru.ulyanaab.lifemates.ui.common.widget.LoadingView
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

    LaunchedEffect(Unit) {
        registerViewModel.attach()
    }

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
                if (password.length < 8) {
                    isPasswordError = true
                    showToast("Минимальная длина пароля: 8 символов", context)
                }
                if (passwordRepeat.isEmpty()) {
                    isPasswordRepeatError = true
                }
                if (login.isNotEmpty() && password.isNotEmpty()
                    && passwordRepeat.isNotEmpty() && password.length >= 8
                ) {
                    if (password != passwordRepeat) {
                        isPasswordError = true
                        isPasswordRepeatError = true
                        showToast(text = "Пароли не совпадают", context)
                    } else {
                        registerViewModel.saveLoginAndPassword(login, password)
                        navController.navigate(AuthNavItem.RegisterSecond.screenRoute)
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
) {
    val isLoading by registerViewModel.isLoading.collectAsState()

    RegisterDialogController(
        registerEvent = registerViewModel.registerEventsFlow.collectAsState(initial = null).value
    )

    if (isLoading) {
        LoadingView()
    }
    RegisterSecondStageView(
        registerViewModel = registerViewModel,
        uploadPhotoViewModel = registerViewModel,
        navController = navController
    )
}

@Composable
fun RegisterSecondStageView(
    registerViewModel: RegisterViewModel,
    uploadPhotoViewModel: UploadPhotoViewModel,
    navController: NavController
) {
    val savedModel = registerViewModel.savedRegisterModel

    var name by remember { mutableStateOf(savedModel?.name ?: "") }
    var description by remember { mutableStateOf(savedModel?.description ?: "") }

    var birthday by remember { mutableStateOf(savedModel?.birthday ?: "") }

    var isNameError by remember { mutableStateOf(false) }
    var chosenGender by remember { mutableStateOf(savedModel?.gender) }
    var chosenShowingGender by remember { mutableStateOf(savedModel?.showingGender) }

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
            UserInfoPhotoBlock(
                uploadPhotoViewModel = uploadPhotoViewModel,
                imageUrl = registerViewModel.getImageUrl()
            )
            PersonalUserInfo(
                name = name,
                birthday = birthday,
                onNameChange = {
                    name = it
                    isNameError = false
                    registerViewModel.saveSnapshot(name = name)
                },
                onBirthdayChange = {
                    birthday = it
                    registerViewModel.saveSnapshot(birthday = birthday)
                },
                onNameClear = {
                    name = ""
                    registerViewModel.saveSnapshot(name = name)
                },
                isNameError = isNameError
            )
            GenderChoiceBlock(
                elements = registerViewModel.getGenderModels(),
                onChoiceChanged = {
                    chosenGender = it
                    registerViewModel.saveSnapshot(gender = chosenGender)
                }
            )
            ShowingGenderChoiceBlock(
                elements = registerViewModel.getShowingGenderModels(),
                onChoiceChanged = {
                    chosenShowingGender = it
                    registerViewModel.saveSnapshot(showingGender = chosenShowingGender)
                }
            )
            InterestsChoiceBlock(
                elements = registerViewModel.getChosenInterests(),
                onChangeButtonClick = {
                    navController.navigate(AuthNavItem.Interests.screenRoute)
                }
            )
            DescriptionBlock(
                description = description,
                onDescriptionChange = {
                    description = it
                    registerViewModel.saveSnapshot(description = description)
                },
                onDescriptionClear = {
                    description = ""
                    registerViewModel.saveSnapshot(description = description)
                }
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
                            chosenGender = chosenGender,
                            showingGender = chosenShowingGender,
                            onNameError = { isNameError = true },
                            isPhotoUploaded = registerViewModel.isPhotoUploaded()
                        )
                    ) {
                        registerViewModel.onRegisterClick(
                            name = name,
                            birthday = birthday,
                            gender = chosenGender,
                            showingGender = chosenShowingGender,
                            description = description,
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun RegisterDialogController(
    registerEvent: RegisterEvent?
) {
    if (registerEvent != null) {
        val openDialog = remember { mutableStateOf(false) }
        var title by remember { mutableStateOf("") }
        var text by remember { mutableStateOf<String?>(null) }

        when (registerEvent.type) {
            RegisterEventType.REGISTRATION_FAILED -> {
                openDialog.value = true
                title = "Не удалось зарегистрироваться"
                text = "Возможно, вы придумали слабый пароль. " +
                        "Либо пользователь с такой почтой уже существует"
            }
            RegisterEventType.UNKNOWN_ERROR -> {
                openDialog.value = true
                title = "Произошла неизвестная ошибка"
            }
        }

        InfoDialog(
            openDialog = openDialog,
            title = title,
            text = text,
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
