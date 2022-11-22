package ru.ulyanaab.lifemates.ui.common.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ulyanaab.lifemates.ui.common.theme.PinkMain
import ru.ulyanaab.lifemates.ui.common.theme.Shapes
import ru.ulyanaab.lifemates.ui.common.theme.Typography

@Composable
fun BadgeNew(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = Shapes.small,
        backgroundColor = PinkMain,
        elevation = 4.dp
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .background(color = Color.Transparent),
            text = "New",
            style = Typography.body1.copy(color = Color.White, fontSize = 12.sp)
        )
    }
}

@Preview
@Composable
fun BadgePreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BadgeNew(Modifier.align(Alignment.Center))
    }
}
