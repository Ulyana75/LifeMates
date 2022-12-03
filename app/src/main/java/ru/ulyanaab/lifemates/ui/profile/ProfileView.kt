package ru.ulyanaab.lifemates.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.common.UploadPhotoViewModel
import ru.ulyanaab.lifemates.ui.common.utils.showToast
import ru.ulyanaab.lifemates.ui.common.widget.ContactsBlock
import ru.ulyanaab.lifemates.ui.common.widget.DescriptionBlock
import ru.ulyanaab.lifemates.ui.common.widget.GenderChoiceBlock
import ru.ulyanaab.lifemates.ui.common.widget.InterestsChoiceBlock
import ru.ulyanaab.lifemates.ui.common.widget.LoadingView
import ru.ulyanaab.lifemates.ui.common.widget.PersonalUserInfo
import ru.ulyanaab.lifemates.ui.common.widget.ShowingGenderChoiceBlock
import ru.ulyanaab.lifemates.ui.common.widget.TopBar
import ru.ulyanaab.lifemates.ui.common.widget.UserInfoButton
import ru.ulyanaab.lifemates.ui.common.widget.UserInfoPhotoBlock
import ru.ulyanaab.lifemates.ui.common.widget.validateInputs

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
) {
    LaunchedEffect(Unit) {
        profileViewModel.attach()
    }

    val isLoading by profileViewModel.isLoading.collectAsState()

    if (isLoading) {
        LoadingView(backgroundColor = Color.White)
    }
    ProfileView(profileViewModel = profileViewModel, uploadPhotoViewModel = profileViewModel)
}

@Composable
fun ProfileView(
    profileViewModel: ProfileViewModel,
    uploadPhotoViewModel: UploadPhotoViewModel
) {
    val isModelReady by profileViewModel.isModelReady.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (isModelReady) {
            val userUiModel by profileViewModel.profileState.collectAsState()
            val interests by profileViewModel.interestsFlow.collectAsState()

            var name by remember { mutableStateOf(userUiModel?.name ?: "") }
            var description by remember { mutableStateOf(userUiModel?.description ?: "") }
            var telegram by remember { mutableStateOf(userUiModel?.telegram ?: "") }
            var vk by remember { mutableStateOf(userUiModel?.vk ?: "") }
            var viber by remember { mutableStateOf(userUiModel?.viber ?: "") }
            var whatsapp by remember { mutableStateOf(userUiModel?.whatsapp ?: "") }
            var instagram by remember { mutableStateOf(userUiModel?.instagram ?: "") }

            var birthday by remember { mutableStateOf(userUiModel?.birthday ?: "") }

            var isNameError by remember { mutableStateOf(false) }
            var isContactsError by remember { mutableStateOf(false) }
            var chosenGender by remember {
                mutableStateOf(
                    profileViewModel.getGenderModels().find { it.isChosen }
                )
            }
            var chosenShowingGender by remember {
                mutableStateOf(
                    profileViewModel.getShowingGenderModels().find { it.isChosen }
                )
            }
            var chosenInterests by remember {
                mutableStateOf(
                    profileViewModel.getInterestsModels().filter { it.isChosen }
                )
            }

            ProfileTopBar(profileViewModel::onExitClick)

            Column(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .verticalScroll(rememberScrollState())
                    .weight(1f, fill = false),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserInfoPhotoBlock(
                    uploadPhotoViewModel = uploadPhotoViewModel,
                    imageUrl = userUiModel?.imageUrl
                )
                PersonalUserInfo(
                    name = name,
                    birthday = birthday,
                    onNameChange = {
                        name = it
                        isNameError = false
                    },
                    onBirthdayChange = { birthday = it },
                    onNameClear = { name = "" },
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
                if (interests.isNotEmpty()) {
                    InterestsChoiceBlock(
                        elements = profileViewModel.getInterestsModels(),
                        onChoiceChanged = {
                            chosenInterests = it
                        }
                    )
                }
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
                                birthday = birthday,
                                gender = chosenGender,
                                showingGender = chosenShowingGender,
                                description = description,
                                telegram = telegram,
                                vk = vk,
                                viber = viber,
                                whatsapp = whatsapp,
                                instagram = instagram,
                                interests = chosenInterests,
                            )
                            showToast("Сохранено", context)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileTopBar(
    onExitClick: () -> Unit
) {
    TopBar(
        text = "Профиль",
        trailIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_exit),
                contentDescription = null,
                modifier = Modifier.clickable {
                    onExitClick.invoke()
                }
            )
        }
    )
}
