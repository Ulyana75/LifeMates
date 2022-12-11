package ru.ulyanaab.lifemates.ui.common.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.domain.report.model.ReportType
import ru.ulyanaab.lifemates.ui.common.theme.Typography
import ru.ulyanaab.lifemates.ui.common.utils.showToast

@Composable
fun ReportsMenu(onReportClick: (ReportType) -> Unit) {

    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Icon(
        painter = painterResource(id = R.drawable.ic_more),
        contentDescription = null,
        modifier = Modifier.clickable {
            showMenu = true
        }
    )

    PopupMenu(
        menuItems = listOf("Агрессия", "Сексуальное домогательство", "Фейковый профиль"),
        onClickCallbacks = listOf(
            {
                onReportClick(ReportType.AGGRESSIVENESS)
                showToast("Жалоба отправлена", context)
            },
            {
                onReportClick(ReportType.HARASSMENT)
                showToast("Жалоба отправлена", context)
            },
            {
                onReportClick(ReportType.PROFILE_CHEATING)
                showToast("Жалоба отправлена", context)
            }
        ),
        showMenu = showMenu,
        onDismiss = { showMenu = false },
    )
}

@Composable
fun PopupMenu(
    menuItems: List<String>,
    onClickCallbacks: List<() -> Unit>,
    showMenu: Boolean,
    onDismiss: () -> Unit,
) {
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { onDismiss() },
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 5.dp),
            text = "Пожаловаться на:",
            style = Typography.body1.copy(color = Color.Black),
            textAlign = TextAlign.Center
        )
        menuItems.forEachIndexed { index, item ->
            DropdownMenuItem(onClick = {
                onDismiss()
                onClickCallbacks[index].invoke()
            }) {
                Text(
                    text = item,
                    style = Typography.body2.copy(color = Color.Black),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
