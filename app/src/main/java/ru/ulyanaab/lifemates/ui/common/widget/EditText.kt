package ru.ulyanaab.lifemates.ui.common.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.common.theme.GreyDark
import ru.ulyanaab.lifemates.ui.common.theme.GreyHint
import ru.ulyanaab.lifemates.ui.common.theme.GreyLight
import ru.ulyanaab.lifemates.ui.common.theme.Shapes
import ru.ulyanaab.lifemates.ui.common.theme.Typography

@Composable
fun EditText(
    modifier: Modifier = Modifier,
    hint: String = "",
    isError: Boolean = false,
    value: String = "",
    onValueChange: (String) -> Unit,
    onClearClicked: () -> Unit,
    isPassword: Boolean = false,
    maxCharacters: Int? = null,
    height: Dp = Size.S,
) {
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = {
                if (maxCharacters == null || it.length <= maxCharacters) {
                    onValueChange.invoke(it)
                } else {
                    onValueChange.invoke(it.substring(0, maxCharacters - 1))
                }
            },
            modifier = Modifier
                .background(
                    color = GreyLight,
                    shape = Shapes.medium
                )
                .border(
                    width = 1.dp,
                    color = if (isError) Color.Red else Color.Transparent,
                    shape = Shapes.medium
                )
                .height(height)
                .fillMaxWidth()
                .focusRequester(focusRequester),
            singleLine = height == Size.S,
            textStyle = Typography.caption.copy(color = Color.Black),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = if (isPassword)
                KeyboardOptions(keyboardType = KeyboardType.Password)
            else KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            decorationBox = { innerTextField ->
                Row(
                    Modifier.padding(start = 16.dp, end = 13.5.dp),
                    verticalAlignment = if (height == Size.S)
                        Alignment.CenterVertically else Alignment.Top
                ) {
                    Box(
                        Modifier
                            .weight(1f)
                            .padding(
                                vertical = if (height == Size.S)
                                    0.dp else 11.dp
                            )
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = hint,
                                color = GreyHint,
                                style = Typography.caption,
                            )
                        }
                        innerTextField()
                    }
                    Box(
                        modifier = Modifier
                            .height(Size.S)
                            .clickable {
                                onClearClicked.invoke()
                                focusRequester.requestFocus()
                            }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = null,
                            tint = GreyDark,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        )
        if (maxCharacters != null) {
            Text(
                text = "${value.length}/$maxCharacters",
                style = Typography.subtitle1.copy(color = GreyDark),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.End)
            )
        }
    }
}

@Preview
@Composable
fun EditTextPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        EditText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            hint = "Логин",
            onValueChange = {},
            onClearClicked = {},
            maxCharacters = 200,
            height = 132.dp
        )
        EditText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            hint = "Логин",
            onValueChange = {},
            onClearClicked = {},
            maxCharacters = 200,
        )
    }
}

object Size {
    val S = 44.dp
    val M = 88.dp
    val L = 132.dp
}
