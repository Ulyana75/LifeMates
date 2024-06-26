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
import androidx.navigation.NavController
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.common.UploadPhotoViewModel
import ru.ulyanaab.lifemates.ui.common.navigation.main.MainNavItem
import ru.ulyanaab.lifemates.ui.common.utils.showToast
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
    navController: NavController,
) {
    LaunchedEffect(Unit) {
        profileViewModel.attach()
    }

    val isLoading by profileViewModel.isLoading.collectAsState()

    if (isLoading) {
        LoadingView(backgroundColor = Color.White)
    }
    ProfileView(
        profileViewModel = profileViewModel,
        uploadPhotoViewModel = profileViewModel,
        navController = navController,
    )
}

@Composable
fun ProfileView(
    profileViewModel: ProfileViewModel,
    uploadPhotoViewModel: UploadPhotoViewModel,
    navController: NavController,
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

            var name by remember { mutableStateOf(userUiModel?.name ?: "") }
            var description by remember { mutableStateOf(userUiModel?.description ?: "") }

            var birthday by remember { mutableStateOf(userUiModel?.birthday ?: "") }

            var isNameError by remember { mutableStateOf(false) }
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
                InterestsChoiceBlock(
                    elements = profileViewModel.getChosenInterests(),
                    onChangeButtonClick = {
                        navController.navigate(MainNavItem.Interests.screenRoute)
                    }
                )
                DescriptionBlock(
                    description = description,
                    onDescriptionChange = { description = it },
                    onDescriptionClear = { description = "" }
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
                                chosenGender = chosenGender,
                                showingGender = chosenShowingGender,
                                onNameError = { isNameError = true },
                            )
                        ) {
                            profileViewModel.onSaveClick(
                                name = name,
                                birthday = birthday,
                                gender = chosenGender,
                                showingGender = chosenShowingGender,
                                description = description,
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
