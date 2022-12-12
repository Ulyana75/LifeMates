package ru.ulyanaab.lifemates.ui.common.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.ulyanaab.lifemates.R

@Composable
fun ArrowsView(
    isLeftArrowEnabled: Boolean,
    isRightArrowEnabled: Boolean,
    onLeftArrowClick: () -> Unit,
    onRightArrowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            shape = CircleShape,
            elevation = 4.dp
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color = Color.White)
                    .clickable(enabled = isLeftArrowEnabled) {
                        onLeftArrowClick.invoke()
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(180f)
                        .align(Alignment.Center)
                        .alpha(if (isLeftArrowEnabled) 1f else 0.5f)
                )
            }
        }

        Card(
            shape = CircleShape,
            elevation = 4.dp
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color = Color.White)
                    .clickable(enabled = isRightArrowEnabled) {
                        onRightArrowClick.invoke()
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(if (isRightArrowEnabled) 1f else 0.5f)
                )
            }
        }
    }
}
