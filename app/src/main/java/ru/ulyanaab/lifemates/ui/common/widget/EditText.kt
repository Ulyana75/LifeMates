package ru.ulyanaab.lifemates.ui.common.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    maxLines: Int = 1,
    isPassword: Boolean = false
) {

    Box(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = { onValueChange.invoke(it) },
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
                .height(44.dp)
                .fillMaxWidth(),
            maxLines = maxLines,
            textStyle = Typography.caption.copy(color = Color.Black),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
            decorationBox = { innerTextField ->
                Row(
                    Modifier.padding(start = 16.dp, end = 13.5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = hint,
                                color = GreyHint,
                                style = Typography.caption,
                            )
                        }
                        innerTextField()
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            onClearClicked.invoke()
                        },
                        tint = GreyDark
                    )
                }
            }
        )
    }
}

@Preview
@Composable
fun EditTextPreview() {
    EditText(
        modifier = Modifier.fillMaxWidth(),
        hint = "Логин",
        onValueChange = {},
        onClearClicked = {}
    )
}
