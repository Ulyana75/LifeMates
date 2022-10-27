package ru.ulyanaab.lifemates.ui.common.widget

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.common.theme.GreyDark
import ru.ulyanaab.lifemates.ui.common.theme.GreyLight
import ru.ulyanaab.lifemates.ui.common.theme.Shapes

@Composable
fun BoxScope.PhotoWithProgress(
    imageUri: Uri,
    isProgressVisible: Boolean,
    isFailure: Boolean,
    progress: Float,
    onReloadClick: () -> Unit,
) {
    SubcomposeAsyncImage(
        model = imageUri,
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center),
        contentScale = ContentScale.Crop,
        loading = { PhotoLoadingPlaceholder() },
        error = { PhotoPlaceholder() }
    )
    if (isProgressVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.5f))
        ) {
            CircularProgressIndicator(
                progress = progress,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(50.dp)
            )
        }
    }
    if (isFailure) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.5f))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_try_again),
                contentDescription = null,
                Modifier.clickable {
                    onReloadClick.invoke()
                }
            )
        }
    }
}

@Composable
fun BoxScope.PhotoOrPlaceholder(
    imageUrl: String? = null,
) {
    if (imageUrl != null) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )
    } else {
        PhotoPlaceholder()
    }
}

@Composable
fun PhotoPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(Shapes.small)
            .background(color = GreyLight)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_image),
            contentDescription = null,
            modifier = Modifier
                .width(50.dp)
                .align(Alignment.Center),
            tint = GreyDark
        )
    }
}

@Composable
fun PhotoLoadingPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = GreyDark,
            modifier = Modifier
                .align(Alignment.Center)
                .size(50.dp)
        )
    }
}
