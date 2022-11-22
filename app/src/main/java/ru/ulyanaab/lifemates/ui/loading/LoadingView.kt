package ru.ulyanaab.lifemates.ui.loading

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import ru.ulyanaab.lifemates.ui.common.navigation.general.GeneralNavItem
import ru.ulyanaab.lifemates.ui.common.theme.BlueMain
import ru.ulyanaab.lifemates.ui.common.theme.PinkMain
import ru.ulyanaab.lifemates.ui.common.theme.PurpleMain
import ru.ulyanaab.lifemates.ui.common.widget.MainHeartIcon

@Composable
fun LoadingScreen(
    loadingViewModel: LoadingViewModel,
    navController: NavController
) {
    LoadingView()

    LaunchedEffect(Unit) {
        loadingViewModel.attach()
    }

    val state by loadingViewModel.actualAuthState.collectAsState()

    LaunchedEffect(state) {
        delay(1000)
        if (state != null) {
            navController.navigate(GeneralNavItem.MainOrAuth.screenRoute) {
                popUpTo(GeneralNavItem.Loading.screenRoute) {
                    inclusive = true
                }
            }
        }
    }
}

@Composable
fun LoadingView() {
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = PurpleMain,
        targetValue = PinkMain,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val size by infiniteTransition.animateFloat(
        initialValue = 50f,
        targetValue = 75f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val degree by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = color)
    ) {
        MainHeartIcon(
            modifier = Modifier
                .align(Alignment.Center)
                .rotate(degree)
                .shadow(elevation = 4.dp, shape = CircleShape),
            circleColor = BlueMain,
            size = size.dp
        )
    }
}

@Preview
@Composable
fun LoadingScreenPreview() {
    Surface {
        LoadingView()
    }
}
