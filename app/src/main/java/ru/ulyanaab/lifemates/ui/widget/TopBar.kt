package ru.ulyanaab.lifemates.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.theme.GreyLine
import ru.ulyanaab.lifemates.ui.theme.Typography

@Composable
fun TopBar(
    leadIcon: (@Composable () -> Unit)? = null,
    trailIcon: (@Composable () -> Unit)? = null,
    text: String = ""
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
    ) {
        Box(
            Modifier.padding(top = 23.dp, bottom = 23.dp, start = 12.dp, end = 12.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = text,
                style = Typography.h3.copy(color = Color.Black),
                textAlign = TextAlign.Center
            )
            if (leadIcon != null) {
                Box(modifier = Modifier.align(Alignment.CenterStart)) {
                    leadIcon.invoke()
                }
            }
            if (trailIcon != null) {
                Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                    trailIcon.invoke()
                }
            }
        }
        Divider(
            color = GreyLine,
            thickness = 1.dp
        )
    }
}

@Preview
@Composable
fun TopBarPreview() {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)) {
        TopBar(
            text = "Профиль",
            leadIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = null)
            },
        )
    }
}
