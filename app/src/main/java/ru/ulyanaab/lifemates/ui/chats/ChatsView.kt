package ru.ulyanaab.lifemates.ui.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import ru.ulyanaab.lifemates.ui.common.navigation.main.MainNavItem
import ru.ulyanaab.lifemates.ui.common.theme.GreyDark
import ru.ulyanaab.lifemates.ui.common.theme.GreyLight
import ru.ulyanaab.lifemates.ui.common.theme.PinkMain
import ru.ulyanaab.lifemates.ui.common.theme.Shapes
import ru.ulyanaab.lifemates.ui.common.theme.Typography
import ru.ulyanaab.lifemates.ui.common.widget.Button
import ru.ulyanaab.lifemates.ui.common.widget.PhotoLoadingPlaceholder
import ru.ulyanaab.lifemates.ui.common.widget.PhotoPlaceholder

@Composable
fun ChatsScreen(
    navController: NavController,
    chatsViewModel: ChatsViewModel
) {
    ChatsView(navController = navController, chatsViewModel = chatsViewModel)
}

@Composable
fun ChatsView(
    navController: NavController,
    chatsViewModel: ChatsViewModel,
) {
    LaunchedEffect(Unit) {
        chatsViewModel.attach()
    }

    val hasMatches by chatsViewModel.hasMatches.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GreyLight)
    ) {
        if (hasMatches) {
            MatchesCard {
                navController.navigate(MainNavItem.Matches.screenRoute)
            }
        }
        ChatsList()
    }
}

@Composable
fun MatchesCard(
    onShowMatchesClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(21.dp)
            .clip(Shapes.large)
            .shadow(elevation = 2.dp, shape = Shapes.large)
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ваши метчи",
            style = Typography.body1.copy(color = Color.Black),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        Button(
            onClick = onShowMatchesClick,
            text = "Посмотреть",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
fun ChatsList() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(Shapes.large)
            .background(color = Color.White),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Здесь скоро будут чаты.\nА пока - посмотрите свои метчи\n(если они есть)",
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
                style = Typography.body1.copy(color = GreyDark)
            )
        }
    }
}

@Composable
fun ChatView(model: ChatUiModel) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.weight(1f)) {
            SubcomposeAsyncImage(
                model = model.userImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                loading = {
                    PhotoLoadingPlaceholder()
                },
                error = {
                    PhotoPlaceholder()
                }
            )

            Column {
                Text(
                    text = model.userName,
                    style = Typography.body1.copy(color = PinkMain)
                )
                Text(
                    text = model.message,
                    style = Typography.body1.copy(color = PinkMain)
                )
            }
        }

        if (model.unreadCount != 0) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        color = PinkMain,
                        shape = CircleShape
                    )
            ) {
                Text(
                    text = model.unreadCount.toString(),
                    style = Typography.subtitle1.copy(color = Color.White)
                )
            }
        }
    }
}

@Preview
@Composable
fun ChatPreview() {
    Surface {
        ChatView(
            model = ChatUiModel(
                id = 0,
                userName = "Ryan",
                userImageUrl = "",
                message = "Вы: Эй Дора, готова?",
                unreadCount = 5
            )
        )
    }
}
