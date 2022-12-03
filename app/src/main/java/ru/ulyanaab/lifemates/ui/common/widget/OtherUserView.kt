package ru.ulyanaab.lifemates.ui.common.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.ulyanaab.lifemates.ui.common.model.OtherUserUiModel
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel
import ru.ulyanaab.lifemates.ui.common.theme.GreyDark
import ru.ulyanaab.lifemates.ui.common.theme.GreyLight
import ru.ulyanaab.lifemates.ui.common.theme.Shapes
import ru.ulyanaab.lifemates.ui.common.theme.Typography
import ru.ulyanaab.lifemates.ui.feed.LikeDislikeSliderWithPrompt

private val PHOTO_HEIGHT = 450.dp

object CardOffset {
    val S = 60.dp
    val M = 90.dp
    val L = 120.dp
}

@Composable
fun OtherUserView(
    model: OtherUserUiModel,
    cardOffset: Dp,
    bottomContent: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GreyLight)
    ) {
        Box(modifier = Modifier.height(PHOTO_HEIGHT)) {
            PhotoOrPlaceholder(model.imageUrl)
        }
        DescriptionBlock(
            model = model,
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = PHOTO_HEIGHT - cardOffset,
                    bottom = 16.dp
                )
                .align(Alignment.TopCenter),
            bottomContent = bottomContent
        )
    }
}

@Composable
fun DescriptionBlock(
    model: OtherUserUiModel,
    modifier: Modifier = Modifier,
    bottomContent: (@Composable () -> Unit)? = null
) {
    Card(
        shape = Shapes.large,
        elevation = 4.dp,
        backgroundColor = Color.White,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleSubtitle(
                title = model.title,
                subtitle = model.subtitle
            )
            if (model.interests.isNotEmpty()) {
                Interests(interests = model.interests)
            }
            if (model.description != null) {
                Description(description = model.description)
            }
            Box(modifier = Modifier.padding(top = 15.dp)) {
                bottomContent?.invoke()
            }
        }
    }
}

@Composable
fun TitleSubtitle(
    title: String,
    subtitle: String,
) {
    Text(
        text = title,
        style = Typography.h4.copy(color = Color.Black),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 5.dp, start = 21.dp, end = 21.dp)
    )
    Text(
        text = subtitle,
        style = Typography.body1.copy(color = GreyDark),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp, start = 21.dp, end = 21.dp)
    )
}

@Composable
fun Interests(interests: List<String>) {
    RoundedBlocksWithoutChoice(
        elementsList = interests.map {
            RoundedBlockUiModel(
                text = it,
                isChosen = true,
            )
        },
        modifier = Modifier.padding(bottom = 15.dp)
    )
}

@Composable
fun ColumnScope.Description(description: String) {
    Column(
        modifier = Modifier
            .padding(start = 21.dp, end = 21.dp)
            .verticalScroll(rememberScrollState())
            .weight(weight = 2f, fill = false)
    ) {
        Text(
            text = description,
            style = Typography.body1.copy(color = Color.Black),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun OtherUserPreview() {
    OtherUserView(
        model = OtherUserUiModel(
            id = 1,
            title = "Ryan Ryan Ryan Ryan Ryan Ryan , 36",
            subtitle = "Los Angeles, USA",
            description = "I'm giving you a night call to tell you how I feel. I want to drive you through the night, down the hills. I'm giving you a night call to tell you how I feel. I want to drive you through the night, down the hills. I'm giving you a night call to tell you how I feel. I want to drive you through the night, down the hills. I'm giving you a night call to tell you how I feel. I want to drive you through the night, down the hills. I'm giving you a night call to tell you how I feel. I want to drive you through the night, down the hills. I'm giving you a night call to tell you how I feel. I want to drive you through the night, down the hills. I'm giving you a night call to tell you how I feel. I want to drive you through the night, down the hills.",
            imageUrl = null,
            contacts = emptyList(),
            interests = listOf("Drive", "Nightcall", "Pyaterochka")
        ),
        cardOffset = CardOffset.S,
        bottomContent = {
            LikeDislikeSliderWithPrompt(
                userModel = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 21.dp, end = 21.dp, bottom = 16.dp),
                onLike = {},
                onDislike = {}
            )
        }
    )
}
