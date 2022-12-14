package ru.ulyanaab.lifemates.ui.common.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex

@Composable
fun LoadingView(
    backgroundColor: Color = Color.Black.copy(alpha = 0.5f)
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
            .zIndex(3f)
    ) {
        CircularProgressIndicator(Modifier.align(Alignment.Center))
    }
}

@Preview
@Composable
fun LoadingPreview() {
    LoadingView()
}
