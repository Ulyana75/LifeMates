package ru.ulyanaab.lifemates.ui.common.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.common.model.OtherUserUiModel
import ru.ulyanaab.lifemates.ui.common.theme.GreyDark
import ru.ulyanaab.lifemates.ui.common.theme.GreyLight
import ru.ulyanaab.lifemates.ui.common.theme.Shapes
import ru.ulyanaab.lifemates.ui.common.theme.Typography

@Composable
fun OtherUserView(
    model: OtherUserUiModel,
    descriptionBottomContent: (@Composable () -> Unit)? = null,
    bottomContent: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GreyLight)
    ) {
        Box(modifier = Modifier.height(450.dp)) {
            PhotoOrPlaceholder(model.imageUrl)
            DescriptionWithBottomContentBlock(
                model = model,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 390.dp),
                descriptionBottomContent = descriptionBottomContent,
                bottomContent = bottomContent
            )
        }
    }
}

@Composable
fun DescriptionWithBottomContentBlock(
    model: OtherUserUiModel,
    modifier: Modifier = Modifier,
    descriptionBottomContent: (@Composable () -> Unit)? = null,
    bottomContent: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier
    ) {
        DescriptionBlock(
            model = model,
            bottomContent = descriptionBottomContent
        )
        bottomContent?.invoke()
    }
}

@Composable
fun DescriptionBlock(
    model: OtherUserUiModel,
    modifier: Modifier = Modifier,
    bottomContent: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier
            .clip(Shapes.large)
            .shadow(elevation = 2.dp, shape = Shapes.large)
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = model.title,
            style = Typography.h4.copy(color = Color.Black),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 5.dp, start = 21.dp, end = 21.dp)
        )
        Text(
            text = model.subtitle,
            style = Typography.body1.copy(color = GreyDark),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp, start = 21.dp, end = 21.dp)
        )
        if (model.description != null) {
            Text(
                text = model.description,
                style = Typography.body1.copy(color = Color.Black),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp, start = 21.dp, end = 21.dp)
            )
        }
        bottomContent?.invoke()
    }
}

@Preview
@Composable
fun OtherUserPreview() {
    OtherUserView(
        model = OtherUserUiModel(
            title = "Ryan, 36",
            subtitle = "Los Angeles, USA",
            description = "I'm giving you a night call to tell you how I feel. I want to drive you through the night, down the hills.",
            imageUrl = null
        ),
        descriptionBottomContent = {
        },
        bottomContent = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
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
    )
}
