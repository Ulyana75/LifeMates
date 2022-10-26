package ru.ulyanaab.lifemates.ui.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.krottv.compose.sliders.SliderValueHorizontal
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.common.model.OtherUserUiModel
import ru.ulyanaab.lifemates.ui.common.theme.BlueMain
import ru.ulyanaab.lifemates.ui.common.theme.GreyDark
import ru.ulyanaab.lifemates.ui.common.theme.GreyLight
import ru.ulyanaab.lifemates.ui.common.theme.PinkMain
import ru.ulyanaab.lifemates.ui.common.theme.PurpleMain
import ru.ulyanaab.lifemates.ui.common.theme.Typography
import ru.ulyanaab.lifemates.ui.common.widget.LoadingView
import ru.ulyanaab.lifemates.ui.common.widget.MainHeartIcon
import ru.ulyanaab.lifemates.ui.common.widget.OtherUserView

@Composable
fun FeedScreen(feedViewModel: FeedViewModel) {
    LaunchedEffect(Unit) {
        feedViewModel.attach()
    }

    val isLoading by feedViewModel.isLoading.collectAsState()

    if (isLoading) {
        LoadingView(backgroundColor = Color.White)
    }
    FeedView(feedViewModel = feedViewModel)
}

@Composable
fun FeedView(feedViewModel: FeedViewModel) {
    val uiModel by feedViewModel.currentUserStateFlow.collectAsState()

    uiModel?.let {
        OtherUserView(
            model = it,
            descriptionBottomContent = {
                LikeDislikeSlider(
                    userModel = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 21.dp, end = 21.dp, bottom = 16.dp),
                    onLike = feedViewModel::onLikeClick,
                    onDislike = feedViewModel::onDislikeClick
                )
            },
            bottomContent = {
                ArrowsView(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        )
    }
}

@Composable
fun LikeDislikeSlider(
    userModel: OtherUserUiModel?,
    modifier: Modifier = Modifier,
    onLike: () -> Unit = {},
    onDislike: () -> Unit = {}
) {
    Box(
        modifier = modifier.height(32.dp)
    ) {
        var value by remember(userModel) { mutableStateOf(0.5f) }
        val size = 24 + value * 100 * 0.16
        val rotation = (value * 100 - 50f) * 5.4f
        val color = getColor(value = value)

        SliderValueHorizontal(
            value = value,
            onValueChange = {
                value = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            onValueChangeFinished = {
                when {
                    value > 0.8f -> onDislike.invoke()
                    value < 0.2f -> onLike.invoke()
                    else -> value = 0.5f
                }
            },
            thumbHeightMax = false,
            track = { modifier: Modifier, _, _, _, _ ->
                Box(
                    Modifier
                        .height(28.dp)
                        .fillMaxWidth()
                        .background(
                            color = GreyLight,
                            shape = RoundedCornerShape(100)
                        )
                        .then(modifier)
                )
            },
            thumb = { modifier, _, _, _, _ ->
                MainHeartIcon(
                    modifier = modifier
                        .fillMaxSize()
                        .rotate(rotation),
                    circleColor = color,
                )
            },
            thumbSizeInDp = DpSize(size.dp, size.dp)
        )
    }
}

@Composable
private fun getColor(
    value: Float
): Color {
    val targetColor = if (value > 0.5f) PinkMain else BlueMain
    val startColor = PurpleMain
    val k = if (value > 0.5f) 1 else -1
    val redStep = (targetColor.red - startColor.red) / 50f * k
    val greenStep = (targetColor.green - startColor.green) / 50f * k
    val blueStep = (targetColor.blue - startColor.blue) / 50f * k

    return Color(
        red = startColor.red + (redStep * (value * 100 - 50)),
        green = startColor.green + (greenStep * (value * 100 - 50)),
        blue = startColor.blue + (blueStep * (value * 100 - 50)),
    )
}

@Composable
fun ArrowsView(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_chevron_left),
                contentDescription = null,
            )
            Text(
                text = "дизлайк",
                style = Typography.body1.copy(
                    color = GreyDark,
                    fontSize = 12.sp
                ),
                textAlign = TextAlign.Center
            )
        }
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = null,
            )
            Text(
                text = "лайк",
                style = Typography.body1.copy(
                    color = GreyDark,
                    fontSize = 12.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun LikeDislikeButtonsPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        LikeDislikeSlider(
            null,
            Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )
    }
}
