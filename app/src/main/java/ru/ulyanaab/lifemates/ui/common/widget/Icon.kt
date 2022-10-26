package ru.ulyanaab.lifemates.ui.common.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.common.theme.PinkMain

@Composable
fun MainHeartIcon(
    modifier: Modifier = Modifier,
    circleColor: Color = PinkMain,
    size: Dp = 32.dp,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(circleColor)
            .size(size)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_heart_main),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .padding(start = 5.dp, end = 5.dp, top = 7.65.dp, bottom = 5.65.dp)
        )
    }
}

@Preview
@Composable
fun IconPreview() {
    MainHeartIcon()
}
