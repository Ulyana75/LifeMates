package ru.ulyanaab.lifemates.ui.common.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LoadingView() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)) {
        CircularProgressIndicator(Modifier.align(Alignment.Center))
    }
}
