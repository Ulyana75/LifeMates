package ru.ulyanaab.lifemates.ui.common.utils

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import java.time.format.DateTimeFormatter

fun String.nullIfEmpty(): String? {
    return if (isEmpty()) null else this
}

fun showToast(text: String, context: Context) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

@Composable
fun RequestPermission(
    permissionResultReceived: MutableState<Boolean>,
    onGranted: () -> Unit,
    onNotGranted: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onGranted.invoke()
        } else {
            onNotGranted.invoke()
        }
        permissionResultReceived.value = true
    }

    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }
}

fun dateFormatterUniversal(): DateTimeFormatter {
    return DateTimeFormatter.ofPattern("yyyy-MM-dd")
}

fun dateFormatterUsual(): DateTimeFormatter {
    return DateTimeFormatter.ofPattern("dd.MM.yyyy")
}

fun dateFormatterWithTime(): DateTimeFormatter {
    return DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss")
}

fun dateFormatterForMessages(): DateTimeFormatter {
    return DateTimeFormatter.ofPattern("dd MMMM H:mm")
}
