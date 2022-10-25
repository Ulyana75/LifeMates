package ru.ulyanaab.lifemates.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import ru.ulyanaab.lifemates.ui.common.widget.ContactsBlock
import ru.ulyanaab.lifemates.ui.common.widget.DescriptionBlock
import ru.ulyanaab.lifemates.ui.common.widget.GenderChoiceBlock
import ru.ulyanaab.lifemates.ui.common.widget.LoadingView
import ru.ulyanaab.lifemates.ui.common.widget.PersonalUserInfo
import ru.ulyanaab.lifemates.ui.common.widget.ShowingGenderChoiceBlock
import ru.ulyanaab.lifemates.ui.common.widget.TopBar
import ru.ulyanaab.lifemates.ui.common.widget.UserInfoButton
import ru.ulyanaab.lifemates.ui.common.widget.UserInfoPhotoBlock
import ru.ulyanaab.lifemates.ui.common.widget.validateInputs

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    LaunchedEffect(Unit) {
        profileViewModel.attach()
    }

    val isLoading by profileViewModel.isLoading.collectAsState()

    if (isLoading) {
        LoadingView()
    } else {
        ProfileView(profileViewModel = profileViewModel)
    }
}

@Composable
fun ProfileView(profileViewModel: ProfileViewModel) {
    val userUiModel by profileViewModel.profileState.collectAsState()

    var name by remember { mutableStateOf(userUiModel?.name ?: "") }
    var age by remember { mutableStateOf(userUiModel?.birthday ?: "") }
    var description by remember { mutableStateOf(userUiModel?.description ?: "") }
    var telegram by remember { mutableStateOf(userUiModel?.telegram ?: "") }
    var vk by remember { mutableStateOf(userUiModel?.vk ?: "") }
    var viber by remember { mutableStateOf(userUiModel?.viber ?: "") }
    var whatsapp by remember { mutableStateOf(userUiModel?.whatsapp ?: "") }
    var instagram by remember { mutableStateOf(userUiModel?.instagram ?: "") }

    var isNameError by remember { mutableStateOf(false) }
    var isContactsError by remember { mutableStateOf(false) }
    var chosenGender by remember {
        mutableStateOf(
            profileViewModel.getGenderModels().find { it.isChosen }
        )
    }
    var chosenShowingGender by remember {
        mutableStateOf(
            profileViewModel.getGenderModels().find { it.isChosen }
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ProfileTopBar()

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
                elements = profileViewModel.getGenderModels(),
                onChoiceChanged = { chosenGender = it }
            )
            ShowingGenderChoiceBlock(
                elements = profileViewModel.getShowingGenderModels(),
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
                text = "Сохранить",
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
                        profileViewModel.onSaveClick(
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
fun ProfileTopBar() {
    TopBar(
        text = "Профиль",
    )
}
