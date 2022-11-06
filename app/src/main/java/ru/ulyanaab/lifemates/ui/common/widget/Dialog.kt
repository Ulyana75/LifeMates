package ru.ulyanaab.lifemates.ui.common.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.ulyanaab.lifemates.ui.common.model.ContactUiModel
import ru.ulyanaab.lifemates.ui.common.theme.Shapes
import ru.ulyanaab.lifemates.ui.common.theme.Typography

@Composable
fun InfoDialog(
    openDialog: MutableState<Boolean>,
    title: String,
    text: String? = null,
    onButtonClick: () -> Unit = {},
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
                    SelectionContainer {
                        Text(
                            text = text,
                            style = Typography.body1.copy(color = Color.Black)
                        )
                    }
                }
            } else null,
            shape = Shapes.large
        )
    }
}

@Composable
fun ContactsDialog(
    openDialog: MutableState<Boolean>,
    contacts: List<ContactUiModel>
) {
    var text by remember {
        mutableStateOf("")
    }
    LaunchedEffect(contacts) {
        text = ""
        contacts.forEach {
            text += "${it.name}: ${it.value}\n"
        }
    }
    InfoDialog(
        openDialog = openDialog,
        title = "У нас пока не работают чаты",
        text = "Вместо этого мы дадим вам контакты.\n$text"
    )
}
