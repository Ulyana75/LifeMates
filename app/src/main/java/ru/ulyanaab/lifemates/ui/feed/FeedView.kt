package ru.ulyanaab.lifemates.ui.feed

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.github.krottv.compose.sliders.SliderValueHorizontal
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.common.model.OtherUserUiModel
import ru.ulyanaab.lifemates.ui.common.navigation.main.MainNavItem
import ru.ulyanaab.lifemates.ui.common.theme.BlueMain
import ru.ulyanaab.lifemates.ui.common.theme.GreyDark
import ru.ulyanaab.lifemates.ui.common.theme.GreyLight
import ru.ulyanaab.lifemates.ui.common.theme.PinkMain
import ru.ulyanaab.lifemates.ui.common.theme.PurpleMain
import ru.ulyanaab.lifemates.ui.common.theme.Shapes
import ru.ulyanaab.lifemates.ui.common.theme.Typography
import ru.ulyanaab.lifemates.ui.common.utils.RequestPermission
import ru.ulyanaab.lifemates.ui.common.widget.Button
import ru.ulyanaab.lifemates.ui.common.widget.CardOffset
import ru.ulyanaab.lifemates.ui.common.widget.LoadingView
import ru.ulyanaab.lifemates.ui.common.widget.MainHeartIcon
import ru.ulyanaab.lifemates.ui.common.widget.OtherUserView
import ru.ulyanaab.lifemates.ui.common.widget.PhotoOrPlaceholder

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun FeedScreen(
    feedViewModel: FeedViewModel,
    navController: NavController,
) {
    FeedScreenWithPermissionRequest(
        feedViewModel = feedViewModel,
        navController = navController
    )
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun FeedScreenWithPermissionRequest(
    feedViewModel: FeedViewModel,
    navController: NavController,
) {
    val permissionResultReceived = remember { mutableStateOf(false) }

    SendLocation(
        feedViewModel = feedViewModel,
        permissionResultReceived = permissionResultReceived
    )

    if (permissionResultReceived.value) {
        LaunchedEffect(Unit) {
            feedViewModel.attach()
        }

        val isLoading by feedViewModel.isLoading.collectAsState()

        if (isLoading) {
            LoadingView(backgroundColor = Color.White)
        }
        FeedView(
            feedViewModel = feedViewModel,
            navController = navController
        )
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun FeedView(
    feedViewModel: FeedViewModel,
    navController: NavController,
) {
    val uiModel by feedViewModel.currentUserStateFlow.collectAsState()
    val matchModel by feedViewModel.matchStateFlow.collectAsState()
    val areUsersFinished by feedViewModel.usersAreFinishedFlow.collectAsState()

    matchModel?.let {
        MatchView(
            matchUiModel = it,
            onGoToChatClick = {
                navController.navigate(
                    MainNavItem.SingleChat.screenRoute +
                            "/${it.chatId}" +
                            "/${it.userId}" +
                            "/${it.actualUserName}" +
                            "?imageUrl=${it.imageUrl}"
                )
            }
        )
    }

    if (areUsersFinished) {
        FinishedUsersView()
    } else {

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
                    cardOffset = CardOffset.S,
                    onReportClick = feedViewModel::onReportClick,
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
}

@Composable
fun FinishedUsersView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Text(
            text = "Вы просмотрели всех пользователей. " +
                    "Мы покажем вам больше, когда кто-нибудь еще зарегистрируется",
            textAlign = TextAlign.Center,
            style = Typography.body1.copy(color = GreyDark),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun SendLocation(
    feedViewModel: FeedViewModel,
    permissionResultReceived: MutableState<Boolean>
) {
    val context = LocalContext.current

    val isPermissionGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (isPermissionGranted) {
        LaunchedEffect(isPermissionGranted) {
            feedViewModel.onLocationPermissionGranted {
                permissionResultReceived.value = true
            }
        }
    } else {
        RequestPermission(
            permissionResultReceived = permissionResultReceived,
            onGranted = {
                feedViewModel.onLocationPermissionGranted()
            },
            onNotGranted = {
                feedViewModel.onLocationPermissionNotGranted()
            }
        )
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun MatchView(
    matchUiModel: MatchUiModel,
    onGoToChatClick: () -> Unit
) {
    var needShow by remember(matchUiModel) { mutableStateOf(true) }

    if (needShow) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val height = maxHeight / 2

            Dialog(
                onDismissRequest = { needShow = false },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
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
                                onGoToChatClick.invoke()
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
                    value > 0.7f -> onLike.invoke()
                    value < 0.3f -> onDislike.invoke()
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

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Preview
@Composable
fun LikeDislikeButtonsPreview() {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color = Color.White)
//    ) {
//        MatchView(
//            matchUiModel = MatchUiModel(
//                title = "У вас мэтч с Абобой",
//                imageUrl = null,
//                contacts = emptyList()
//            )
//        )
//    }
}
