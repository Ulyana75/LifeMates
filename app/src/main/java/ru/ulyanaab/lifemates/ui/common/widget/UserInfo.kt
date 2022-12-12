package ru.ulyanaab.lifemates.ui.common.widget

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.ulyanaab.lifemates.ui.common.UploadPhotoViewModel
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel
import ru.ulyanaab.lifemates.ui.common.theme.GreyHint
import ru.ulyanaab.lifemates.ui.common.theme.GreyLight
import ru.ulyanaab.lifemates.ui.common.theme.Shapes
import ru.ulyanaab.lifemates.ui.common.theme.Typography
import ru.ulyanaab.lifemates.ui.common.utils.dateFormatterUsual
import ru.ulyanaab.lifemates.ui.common.utils.showToast
import java.time.LocalDate
import java.util.Calendar

@Composable
fun UserInfoEditText(
    value: String,
    onValueChange: (String) -> Unit,
    onValueClear: () -> Unit,
    hint: String,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
    isError: Boolean = false,
    isPassword: Boolean = false,
    maxCharacters: Int? = null,
    height: Dp,
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
        isPassword = isPassword,
        maxCharacters = maxCharacters,
        height = height
    )
}

@Composable
fun UserInfoDateOfBirth(
    value: String,
    onValueChange: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    val month by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    val day by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, y, m, d ->
            onValueChange(LocalDate.of(y, m + 1, d).format(dateFormatterUsual()))
        },
        year,
        month,
        day
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 45.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = GreyLight,
                    shape = Shapes.medium
                )
                .height(44.dp)
                .fillMaxWidth()
                .clickable {
                    datePickerDialog.show()
                },
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = "Дата рождения",
                    color = GreyHint,
                    style = Typography.caption,
                    modifier = Modifier.padding(start = 16.dp, end = 13.5.dp),
                )
            } else {
                Text(
                    text = value,
                    style = Typography.caption.copy(color = Color.Black),
                    modifier = Modifier.padding(start = 16.dp, end = 13.5.dp),
                )
            }
        }
    }
}

@Composable
fun UserInfoTitleWithInputs(
    inputsCount: Int,
    title: String,
    valueList: List<String>,
    onValueChangeList: List<(String) -> Unit>,
    onValueClearList: List<() -> Unit>,
    hintList: List<String>,
    isErrorList: List<Boolean> = emptyList(),
    isPasswordList: List<Boolean> = emptyList(),
    maxCharactersList: List<Int?> = emptyList(),
    heightsList: List<Dp> = emptyList(),
    needBigBottomPadding: Boolean = true,
) {
    UserInfoBlockTitle(
        text = title,
        paddingBottom = 10.dp
    )
    for (i in 0 until inputsCount) {
        UserInfoEditText(
            value = valueList[i],
            onValueChange = onValueChangeList[i],
            onValueClear = onValueClearList[i],
            hint = hintList[i],
            isError = isErrorList.getOrNull(i) ?: false,
            paddingBottom = if (i == inputsCount - 1 && needBigBottomPadding) 45.dp else 10.dp,
            isPassword = isPasswordList.getOrNull(i) ?: false,
            maxCharacters = maxCharactersList.getOrNull(i),
            height = heightsList.getOrNull(i) ?: Size.S,
        )
    }
}

@Composable
fun UserInfoPhotoBlock(
    uploadPhotoViewModel: UploadPhotoViewModel,
    imageUrl: String? = null
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    LaunchedEffect(imageUri) {
        imageUri?.let {
            uploadPhotoViewModel.onImagePickedFromGallery(it)
        }
    }

    UserInfoBlockTitle(
        text = "Фото",
        paddingTop = 30.dp,
        paddingBottom = 10.dp
    )
    Box(
        modifier = Modifier
            .height(439.dp)
            .fillMaxWidth()
            .clip(Shapes.small)
            .background(color = GreyLight)
    ) {
        if (imageUri != null) {
            PhotoWithProgress(
                imageUri = imageUri!!,
                isProgressVisible = uploadPhotoViewModel.isProgressVisible.collectAsState().value,
                isFailure = uploadPhotoViewModel.isFailure.collectAsState().value,
                progress = uploadPhotoViewModel.progressValue.collectAsState().value,
                onReloadClick = {
                    imageUri?.let {
                        uploadPhotoViewModel.onImagePickedFromGallery(it)
                    }
                },
            )
        } else {
            PhotoOrPlaceholder(imageUrl = imageUrl)
        }
    }
    UserInfoButton(
        text = "Загрузить",
        paddingTop = 20.dp,
        paddingBottom = 45.dp,
        onClick = {
            launcher.launch("image/*")
        }
    )
}

@Composable
fun UserInfoBlockTitle(
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
fun UserInfoButton(
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
fun UserInfoChoiceBlock(
    title: String,
    elements: List<RoundedBlockUiModel>,
    onChoiceChanged: (RoundedBlockUiModel) -> Unit
) {
    UserInfoBlockTitle(
        text = title,
        paddingBottom = 10.dp
    )
    RoundedBlocksSingleChoice(
        onChoiceChanged = onChoiceChanged,
        elementsList = elements,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 45.dp)
    )
}

@Composable
fun InterestsChoiceBlock(
    elements: List<RoundedBlockUiModel>,
    onChangeButtonClick: () -> Unit
) {
    UserInfoBlockTitle(
        text = "Ваши интересы",
        paddingBottom = 10.dp,
    )
    if (elements.isNotEmpty()) {
        RoundedBlocksWithoutChoice(
            elementsList = elements,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        )
    }
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 45.dp),
        onClick = onChangeButtonClick,
        isHighlighted = elements.isEmpty(),
        text = if (elements.isEmpty()) "Выбрать" else "Изменить"
    )
}

@Composable
fun PersonalUserInfo(
    name: String,
    birthday: String,
    onNameChange: (String) -> Unit,
    onBirthdayChange: (String) -> Unit,
    onNameClear: () -> Unit,
    isNameError: Boolean
) {
    UserInfoTitleWithInputs(
        inputsCount = 1,
        title = "Личная информация",
        valueList = listOf(name),
        onValueChangeList = listOf(onNameChange),
        onValueClearList = listOf(onNameClear),
        hintList = listOf("Имя"),
        isErrorList = listOf(isNameError),
        needBigBottomPadding = false,
        maxCharactersList = listOf(30),
    )

    UserInfoDateOfBirth(
        value = birthday,
        onValueChange = onBirthdayChange
    )
}

@Composable
fun GenderChoiceBlock(
    elements: List<RoundedBlockUiModel>,
    onChoiceChanged: (RoundedBlockUiModel) -> Unit
) {
    UserInfoChoiceBlock(
        title = "Ваш пол",
        elements = elements,
        onChoiceChanged = onChoiceChanged
    )
}

@Composable
fun ShowingGenderChoiceBlock(
    elements: List<RoundedBlockUiModel>,
    onChoiceChanged: (RoundedBlockUiModel) -> Unit
) {
    UserInfoChoiceBlock(
        title = "Какой пол вам показывать в ленте",
        elements = elements,
        onChoiceChanged = onChoiceChanged
    )
}

@Composable
fun DescriptionBlock(
    description: String,
    onDescriptionChange: (String) -> Unit,
    onDescriptionClear: () -> Unit
) {
    UserInfoTitleWithInputs(
        inputsCount = 1,
        title = "О себе",
        valueList = listOf(description),
        onValueChangeList = listOf(onDescriptionChange),
        onValueClearList = listOf(onDescriptionClear),
        hintList = listOf("О себе"),
        maxCharactersList = listOf(250),
        heightsList = listOf(Size.L),
    )
}

fun validateInputs(
    context: Context,
    name: String,
    chosenGender: RoundedBlockUiModel?,
    showingGender: RoundedBlockUiModel?,
    onNameError: () -> Unit,
    isPhotoUploaded: Boolean = true,
): Boolean {

    if (name.isEmpty()) {
        onNameError.invoke()
        showToast("Имя не должно быть пустым", context)
        return false
    }
    if (chosenGender == null) {
        showToast("Вы не указали пол", context)
        return false
    }
    if (showingGender == null) {
        showToast("Вы не указали, какой пол показывать в ленте", context)
        return false
    }
    if (!isPhotoUploaded) {
        showToast("Пожалуйста, загрузите фото", context)
        return false
    }

    return true
}
