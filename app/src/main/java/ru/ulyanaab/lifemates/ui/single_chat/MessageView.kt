package ru.ulyanaab.lifemates.ui.single_chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.common.theme.BlueMain
import ru.ulyanaab.lifemates.ui.common.theme.GreyHint
import ru.ulyanaab.lifemates.ui.common.theme.PinkMain
import ru.ulyanaab.lifemates.ui.common.theme.Typography
import java.util.UUID


class RightTriangleEdgeShape : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val trianglePath = Path().apply {
            moveTo(x = 0f, y = 0f)
            lineTo(x = 0f, y = size.height)
            lineTo(x = size.width, y = size.height)
        }
        return Outline.Generic(path = trianglePath)
    }
}

class LeftTriangleEdgeShape : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val trianglePath = Path().apply {
            moveTo(x = size.width, y = 0f)
            lineTo(x = size.width, y = size.height)
            lineTo(x = 0f, y = size.height)
        }
        return Outline.Generic(path = trianglePath)
    }
}

@Composable
fun MessageView(
    message: MessageUiModel,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (message) {
            is MessageUiModel.SentMessageUiModel ->
                SentMessage(message = message)
            is MessageUiModel.StubMessageUiModel ->
                StubMessage(message = message)
        }
    }
}

@Composable
fun SentMessage(
    message: MessageUiModel.SentMessageUiModel
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isFromMe)
            Alignment.End else Alignment.Start
    ) {
        if (message.isFromMe) {
            RightMessage(text = message.text)
        } else {
            LeftMessage(text = message.text)
        }

        Text(
            text = message.date,
            style = Typography.subtitle1.copy(color = GreyHint)
        )
    }
}

@Composable
fun StubMessage(message: MessageUiModel.StubMessageUiModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        RightMessage(
            text = message.text,
            alpha = 0.5f
        )

        if (message.isFailed) {
            Image(
                painter = painterResource(id = R.drawable.ic_error),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        } else {
            CircularProgressIndicator(
                Modifier.size(16.dp),
                color = GreyHint,
                strokeWidth = 2.dp
            )
        }

    }
}

@Composable
fun LeftMessage(text: String) {
    Row(verticalAlignment = Alignment.Bottom) {
        Box(
            modifier = Modifier
                .background(
                    color = BlueMain,
                    shape = LeftTriangleEdgeShape()
                )
                .width(6.dp)
                .height(6.dp)
        )

        Column(
            modifier = Modifier.background(
                color = BlueMain,
                shape = RoundedCornerShape(18.dp, 18.dp, 18.dp, 0.dp)
            )
        ) {
            Text(
                text = text,
                style = Typography.body2.copy(color = Color.White),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
fun RightMessage(
    text: String,
    alpha: Float = 1f,
) {
    BoxWithConstraints {
        val maxWidth = maxWidth
        Row(verticalAlignment = Alignment.Bottom) {
            Column(
                modifier = Modifier
                    .background(
                        color = PinkMain.copy(alpha = alpha),
                        shape = RoundedCornerShape(18.dp, 18.dp, 0.dp, 18.dp)
                    )
                    .widthIn(0.dp, maxWidth - 6.dp)
            ) {
                Text(
                    text = text,
                    style = Typography.body2.copy(color = Color.White),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Box(
                modifier = Modifier
                    .background(
                        color = PinkMain.copy(alpha = alpha),
                        shape = RightTriangleEdgeShape()
                    )
                    .width(6.dp)
                    .height(6.dp)
            )
        }
    }
}

@Preview
@Composable
fun MessagePreview() {
    Surface {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SentMessage(
                message = MessageUiModel.SentMessageUiModel(
                    id = 0,
                    text = "Hello",
                    date = "20.08.2020",
                    isSeen = true,
                    isFromMe = true,
                )
            )

            SentMessage(
                message = MessageUiModel.SentMessageUiModel(
                    id = 0,
                    text = "Hello asd asdasd asdasd" +
                            "asdasdasdas asdasd" +
                            "asdasdasd asdasdasd",
                    date = "20.08.2020",
                    isSeen = true,
                    isFromMe = true,
                )
            )

            StubMessage(
                message = MessageUiModel.StubMessageUiModel(
                    id = UUID.randomUUID(),
                    text = "hello",
                    isFailed = false,
                )
            )
        }

    }
}
