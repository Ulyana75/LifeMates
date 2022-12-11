package ru.ulyanaab.lifemates.ui.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import ru.ulyanaab.lifemates.ui.chats.ChatsViewModel.Companion.CHATS_TILL_END_TO_REQUEST
import ru.ulyanaab.lifemates.ui.common.navigation.main.MainNavItem
import ru.ulyanaab.lifemates.ui.common.theme.GreyDark
import ru.ulyanaab.lifemates.ui.common.theme.GreyLight
import ru.ulyanaab.lifemates.ui.common.theme.GreyLine
import ru.ulyanaab.lifemates.ui.common.theme.PinkMain
import ru.ulyanaab.lifemates.ui.common.theme.Shapes
import ru.ulyanaab.lifemates.ui.common.theme.Typography
import ru.ulyanaab.lifemates.ui.common.widget.Button
import ru.ulyanaab.lifemates.ui.common.widget.LoadingView
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
    val lifecycle = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        chatsViewModel.attach()

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    chatsViewModel.detach()
                }
                Lifecycle.Event.ON_RESUME -> {
                    chatsViewModel.attach()
                }
                else -> Unit
            }
        }

        lifecycle.lifecycle.addObserver(observer)

        onDispose {
            lifecycle.lifecycle.removeObserver(observer)
        }
    }

    val hasMatches by chatsViewModel.hasMatches.collectAsState()
    val chats by chatsViewModel.chatsStateFlow.collectAsState()
    val isLoading by chatsViewModel.isLoading.collectAsState()

    if (isLoading) {
        LoadingView(backgroundColor = Color.White)
    } else {
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
            ChatsList(
                chats = chats,
                requestNext = chatsViewModel::requestNext,
                onChatClick = {
                    navController.navigate(
                        MainNavItem.SingleChat.screenRoute +
                                "/${it.id}" +
                                "/${it.userId}" +
                                "/${it.userName}" +
                                "?imageUrl=${it.userImageUrl}"
                    )
                }
            )
        }
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
fun ChatsList(
    chats: List<ChatUiModel>,
    requestNext: () -> Unit,
    onChatClick: (ChatUiModel) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .clip(Shapes.large)
            .background(color = Color.White),
    ) {
       itemsIndexed(
           items = chats
       ) { index, chat ->
           LaunchedEffect(index) {
               if (chats.size - index == CHATS_TILL_END_TO_REQUEST) {
                   requestNext.invoke()
               }
           }
           ChatView(
               model = chat,
               onChatClick = onChatClick
           )
       }
    }
}

@Composable
fun ChatView(
    model: ChatUiModel,
    onChatClick: (ChatUiModel) -> Unit,
) {
    Column(
        modifier = Modifier.clickable {
            onChatClick.invoke(model)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                        style = Typography.body1.copy(color = GreyDark),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            if (model.unreadCount != 0) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(22.dp)
                        .background(
                            color = PinkMain,
                            shape = CircleShape
                        )
                ) {
                    Text(
                        text = model.unreadCount.toString(),
                        style = Typography.subtitle1.copy(color = Color.White),
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }

        Divider(
            color = GreyLine,
            thickness = 0.5.dp
        )
    }
}

@Preview
@Composable
fun ChatPreview() {
    Surface(Modifier.fillMaxSize()) {
        ChatView(
            model = ChatUiModel(
                id = 0,
                userId = 0,
                userName = "Ryan",
                userImageUrl = "",
                message = "Вы: Эй Дора, готова? adsdas asdasd asd",
                unreadCount = 5
            ),
            {},
        )
    }
}
