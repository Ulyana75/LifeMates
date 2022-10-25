package ru.ulyanaab.lifemates.ui.common.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.ulyanaab.lifemates.ui.common.theme.Shapes
import ru.ulyanaab.lifemates.ui.common.theme.Typography

@Composable
fun InfoDialog(
    openDialog: MutableState<Boolean>,
    title: String,
    text: String? = null,
    onButtonClick: () -> Unit = {}
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            buttons = {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = {
                        openDialog.value = false
                        onButtonClick.invoke()
                    },
                    text = "Понятно",
                    isHighlighted = false
                )
            },
            title = {
                Text(
                    text = title,
                    style = Typography.h2.copy(color = Color.Black)
                )
            },
            text = if (text != null) {
                {
                    Text(
                        text = text,
                        style = Typography.body1.copy(color = Color.Black)
                    )
                }
            } else null,
            shape = Shapes.large
        )
    }
}
