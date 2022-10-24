package ru.ulyanaab.lifemates.ui.register

import android.view.ViewTreeObserver
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.theme.GreyLight
import ru.ulyanaab.lifemates.ui.theme.Shapes
import ru.ulyanaab.lifemates.ui.theme.Typography
import ru.ulyanaab.lifemates.ui.widget.Button
import ru.ulyanaab.lifemates.ui.widget.EditText
import ru.ulyanaab.lifemates.ui.widget.TopBar

@ExperimentalFoundationApi
@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel,
    navController: NavController,
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var isNameError by remember { mutableStateOf(false) }


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
            RegisterPersonalInfoBlock(
                name = name,
                onNameChange = {
                    name = it
                    isNameError = false
                },
                onNameClear = { name = "" },
                age = age,
                onAgeChange = { age = it },
                onAgeClear = { age = "" },
                isNameError = isNameError
            )
            RegisterDescriptionBlock(
                description = description,
                onDescriptionChange = { description = it },
                onDescriptionClear = { description = "" }
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

@ExperimentalFoundationApi
@Composable
fun RegisterEditText(
    value: String,
    onValueChange: (String) -> Unit,
    onValueClear: () -> Unit,
    hint: String,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
    isError: Boolean = false
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()
    val view = LocalView.current
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            scope.launch { bringIntoViewRequester.bringIntoView() }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose { view.viewTreeObserver.removeOnGlobalLayoutListener(listener) }
    }

    EditText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingTop, bottom = paddingBottom)
            .bringIntoViewRequester(bringIntoViewRequester),
        hint = hint,
        isError = isError,
        value = value,
        onValueChange = onValueChange,
        onClearClicked = onValueClear,
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

@ExperimentalFoundationApi
@Composable
fun RegisterPersonalInfoBlock(
    name: String,
    onNameChange: (String) -> Unit,
    onNameClear: () -> Unit,
    isNameError: Boolean,
    age: String,
    onAgeChange: (String) -> Unit,
    onAgeClear: () -> Unit,
) {
    RegisterBlockTitle(
        text = "Личная информация",
        paddingBottom = 10.dp
    )
    RegisterEditText(
        value = name,
        onValueChange = onNameChange,
        onValueClear = onNameClear,
        hint = "Имя",
        isError = isNameError,
        paddingBottom = 10.dp
    )
    RegisterEditText(
        value = age,
        onValueChange = onAgeChange,
        onValueClear = onAgeClear,
        hint = "Возраст",
        paddingBottom = 45.dp
    )
}

@ExperimentalFoundationApi
@Composable
fun RegisterDescriptionBlock(
    description: String,
    onDescriptionChange: (String) -> Unit,
    onDescriptionClear: () -> Unit,
) {
    RegisterBlockTitle(
        text = "О себе",
        paddingBottom = 10.dp
    )
    RegisterEditText(
        value = description,
        onValueChange = onDescriptionChange,
        onValueClear = onDescriptionClear,
        hint = "О себе",
        paddingBottom = 45.dp
    )
}

@ExperimentalFoundationApi
@Preview
@Composable
fun RegisterPreview() {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var isNameError by remember { mutableStateOf(false) }


    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        RegisterTopBar {
            // TODO navigation
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .verticalScroll(rememberScrollState())
                .weight(1f, fill = false),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegisterPhotoBlock {}
            RegisterPersonalInfoBlock(
                name = name,
                onNameChange = {
                    name = it
                    isNameError = false
                },
                onNameClear = { name = "" },
                age = age,
                onAgeChange = { age = it },
                onAgeClear = { age = "" },
                isNameError = isNameError
            )
            RegisterDescriptionBlock(
                description = description,
                onDescriptionChange = { description = it },
                onDescriptionClear = { description = "" }
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
