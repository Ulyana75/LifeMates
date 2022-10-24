package ru.ulyanaab.lifemates.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.ulyanaab.lifemates.ui.theme.GreyLight
import ru.ulyanaab.lifemates.ui.theme.PinkMain
import ru.ulyanaab.lifemates.ui.theme.Shapes
import ru.ulyanaab.lifemates.ui.theme.Typography

@Composable
fun Button(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isHighlighted: Boolean = true,
    text: String,
) {
    Box(modifier = modifier) {
        androidx.compose.material.Button(
            onClick = onClick,
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(),
            shape = Shapes.medium,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (isHighlighted) PinkMain else GreyLight
            )
        ) {
            Text(
                text = text,
                style = Typography.button,
                color = if (isHighlighted) Color.White else Color.Black
            )
        }
    }
}

@Preview
@Composable
fun ButtonPreview() {
    Button(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        text = "Войти",
        onClick = {})
}
