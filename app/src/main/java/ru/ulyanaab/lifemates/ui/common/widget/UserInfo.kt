package ru.ulyanaab.lifemates.ui.common.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.common.UploadPhotoViewModel
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel
import ru.ulyanaab.lifemates.ui.common.theme.GreyDark
import ru.ulyanaab.lifemates.ui.common.utils.showToast
import ru.ulyanaab.lifemates.ui.common.theme.GreyLight
import ru.ulyanaab.lifemates.ui.common.theme.Shapes
import ru.ulyanaab.lifemates.ui.common.theme.Typography

@Composable
fun UserInfoEditText(
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
fun UserInfoTitleWithInputs(
    inputsCount: Int,
    title: String,
    valueList: List<String>,
    onValueChangeList: List<(String) -> Unit>,
    onValueClearList: List<() -> Unit>,
    hintList: List<String>,
    isErrorList: List<Boolean> = emptyList(),
    isPasswordList: List<Boolean> = emptyList()
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
            paddingBottom = if (i == inputsCount - 1) 45.dp else 10.dp,
            isPassword = isPasswordList.getOrNull(i) ?: false
        )
    }
}

@Composable
fun UserInfoPhotoBlock(
    uploadPhotoViewModel: UploadPhotoViewModel,
    imageUrl: String? = null
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        uri?.let {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
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
        if (bitmap != null) {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
            if (uploadPhotoViewModel.isProgressVisible.collectAsState().value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Black.copy(alpha = 0.5f))
                ) {
                    CircularProgressIndicator(
                        progress = uploadPhotoViewModel.progressValue.collectAsState().value,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(50.dp)
                    )
                }
            }
            if (uploadPhotoViewModel.isFailure.collectAsState().value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_try_again),
                        contentDescription = null,
                        Modifier.clickable {
                            imageUri?.let {
                                uploadPhotoViewModel.onImagePickedFromGallery(it)
                            }
                        }
                    )
                }
            }
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
fun BoxScope.PhotoOrPlaceholder(
    imageUrl: String? = null,
) {
    if (imageUrl != null) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = Modifier
                .height(439.dp)
                .fillMaxWidth()
                .clip(Shapes.small)
                .background(color = GreyLight)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_image),
                contentDescription = null,
                modifier = Modifier
                    .width(50.dp)
                    .align(Alignment.Center),
                tint = GreyDark
            )
        }
    }
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
fun PersonalUserInfo(
    name: String,
    age: String,
    onNameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onNameClear: () -> Unit,
    onAgeClear: () -> Unit,
    isNameError: Boolean
) {
    UserInfoTitleWithInputs(
        inputsCount = 2,
        title = "Личная информация",
        valueList = listOf(name, age),
        onValueChangeList = listOf(
            onNameChange,
            onAgeChange
        ),
        onValueClearList = listOf(
            onNameClear,
            onAgeClear
        ),
        hintList = listOf("Имя", "Дата рождения"),
        isErrorList = listOf(isNameError)
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
    )
}

@Composable
fun ContactsBlock(
    telegram: String,
    vk: String,
    viber: String,
    whatsapp: String,
    instagram: String,
    onTelegramChange: (String) -> Unit,
    onVkChange: (String) -> Unit,
    onViberChange: (String) -> Unit,
    onWhatsappChange: (String) -> Unit,
    onInstagramChange: (String) -> Unit,
    onTelegramClear: () -> Unit,
    onVkClear: () -> Unit,
    onViberClear: () -> Unit,
    onWhatsappClear: () -> Unit,
    onInstagramClear: () -> Unit,
    isContactsError: Boolean
) {
    UserInfoTitleWithInputs(
        inputsCount = 5,
        title = "Ваши контакты",
        valueList = listOf(telegram, vk, viber, whatsapp, instagram),
        onValueChangeList = listOf(
            onTelegramChange,
            onVkChange,
            onViberChange,
            onWhatsappChange,
            onInstagramChange,
        ),
        onValueClearList = listOf(
            onTelegramClear,
            onVkClear,
            onViberClear,
            onWhatsappClear,
            onInstagramClear,
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
}

fun validateInputs(
    context: Context,
    name: String,
    telegram: String,
    vk: String,
    viber: String,
    whatsapp: String,
    instagram: String,
    chosenGender: RoundedBlockUiModel?,
    showingGender: RoundedBlockUiModel?,
    onNameError: () -> Unit,
    onContactsError: () -> Unit,
): Boolean {

    if (name.isEmpty()) {
        onNameError.invoke()
        showToast("Имя не должно быть пустым", context)
        return false
    }
    if (telegram.isEmpty() && vk.isEmpty()
        && viber.isEmpty() && whatsapp.isEmpty()
        && instagram.isEmpty()
    ) {
        onContactsError.invoke()
        showToast("Заполните хотя бы один контакт", context)
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

    return true
}
