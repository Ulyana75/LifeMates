package ru.ulyanaab.lifemates.ui.feed

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.github.krottv.compose.sliders.SliderValueHorizontal
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.common.model.ContactUiModel
import ru.ulyanaab.lifemates.ui.common.model.OtherUserUiModel
import ru.ulyanaab.lifemates.ui.common.theme.BlueMain
import ru.ulyanaab.lifemates.ui.common.theme.GreyDark
import ru.ulyanaab.lifemates.ui.common.theme.GreyLight
import ru.ulyanaab.lifemates.ui.common.theme.PinkMain
import ru.ulyanaab.lifemates.ui.common.theme.PurpleMain
import ru.ulyanaab.lifemates.ui.common.theme.Shapes
import ru.ulyanaab.lifemates.ui.common.theme.Typography
import ru.ulyanaab.lifemates.ui.common.widget.Button
import ru.ulyanaab.lifemates.ui.common.widget.InfoDialog
import ru.ulyanaab.lifemates.ui.common.widget.LoadingView
import ru.ulyanaab.lifemates.ui.common.widget.MainHeartIcon
import ru.ulyanaab.lifemates.ui.common.widget.OtherUserView
import ru.ulyanaab.lifemates.ui.common.widget.PhotoOrPlaceholder

@ExperimentalAnimationApi
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

@ExperimentalAnimationApi
@Composable
fun FeedView(feedViewModel: FeedViewModel) {
    val uiModel by feedViewModel.currentUserStateFlow.collectAsState()
    val matchModel by feedViewModel.matchStateFlow.collectAsState()

    matchModel?.let {
        MatchView(matchUiModel = it)
    }

    AnimatedContent(
        targetState = uiModel,
        transitionSpec = {
            fadeIn(animationSpec = tween(400)) with
                    fadeOut(animationSpec = tween(400))
        }
    ) { targetState ->
        targetState?.let {
            OtherUserView(
                model = it,
                bottomContent = {
                    LikeDislikeSliderWithPrompt(
                        userModel = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 21.dp, end = 21.dp, bottom = 16.dp),
                        onLike = feedViewModel::onLikeClick,
                        onDislike = feedViewModel::onDislikeClick
                    )
                }
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun MatchView(
    matchUiModel: MatchUiModel
) {
    var needShow by remember(matchUiModel) { mutableStateOf(true) }
    val openDialog = remember { mutableStateOf(false) }

    ContactsDialog(
        openDialog = openDialog,
        contacts = matchUiModel.contacts
    )

    if (needShow) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
                .background(color = Color.Black.copy(alpha = 0.7f))
        ) {
            val height = maxHeight / 2

            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .align(Alignment.Center)
                    .clip(Shapes.large)
                    .shadow(elevation = 4.dp, shape = Shapes.large)
                    .background(Color.White),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.height(height)) {
                        PhotoOrPlaceholder(matchUiModel.imageUrl)
                    }
                    Text(
                        text = matchUiModel.title,
                        style = Typography.h4.copy(color = Color.Black),
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        onClick = {
                            openDialog.value = true
                        },
                        text = "Начать общение!"
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(40.dp)
                        .clickable {
                            needShow = false
                        }
                        .shadow(elevation = 4.dp, shape = CircleShape)
                )
            }
        }
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

@Composable
fun LikeDislikeSliderWithPrompt(
    userModel: OtherUserUiModel?,
    modifier: Modifier = Modifier,
    onLike: () -> Unit = {},
    onDislike: () -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        LikeDislikeSlider(
            userModel = userModel,
            modifier = Modifier.fillMaxWidth(),
            onLike = onLike,
            onDislike = onDislike
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "дизлайк",
                style = Typography.subtitle1.copy(color = GreyDark)
            )
            Text(
                text = "лайк",
                style = Typography.subtitle1.copy(color = GreyDark)
            )
        }
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
                    value > 0.8f -> onLike.invoke()
                    value < 0.2f -> onDislike.invoke()
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

@ExperimentalAnimationApi
@Preview
@Composable
fun LikeDislikeButtonsPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        MatchView(
            matchUiModel = MatchUiModel(
                title = "У вас мэтч с Абобой",
                imageUrl = null,
                contacts = emptyList()
            )
        )
    }
}
