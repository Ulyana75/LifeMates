package ru.ulyanaab.lifemates.ui.single_chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.common.navigation.main.MainNavItem
import ru.ulyanaab.lifemates.ui.common.theme.GreyDark
import ru.ulyanaab.lifemates.ui.common.theme.GreyLight
import ru.ulyanaab.lifemates.ui.common.theme.Typography
import ru.ulyanaab.lifemates.ui.common.widget.EditText
import ru.ulyanaab.lifemates.ui.common.widget.LoadingView
import ru.ulyanaab.lifemates.ui.common.widget.MainHeartIcon
import ru.ulyanaab.lifemates.ui.common.widget.PhotoLoadingPlaceholder
import ru.ulyanaab.lifemates.ui.common.widget.PhotoPlaceholder
import ru.ulyanaab.lifemates.ui.common.widget.ReportsMenu
import ru.ulyanaab.lifemates.ui.common.widget.Size
import ru.ulyanaab.lifemates.ui.common.widget.TopBar
import ru.ulyanaab.lifemates.ui.single_chat.SingleChatViewModel.Companion.MESSAGES_TILL_END_TO_REQUEST

@ExperimentalPagerApi
@Composable
fun SingleChatScreen(
    singleChatViewModel: SingleChatViewModel,
    navController: NavController,
    config: SingleChatScreenConfig,
) {
    val lifecycle = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        singleChatViewModel.attach()

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    singleChatViewModel.detach()
                }
                Lifecycle.Event.ON_RESUME -> {
                    singleChatViewModel.attach()
                }
                else -> Unit
            }
        }

        lifecycle.lifecycle.addObserver(observer)

        onDispose {
            lifecycle.lifecycle.removeObserver(observer)
        }
    }

    val messages by singleChatViewModel.messagesStateFlow.collectAsState()
    val isLoading by singleChatViewModel.isLoading.collectAsState()
    val themes by singleChatViewModel.themesStateFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GreyLight),
    ) {
        TopBar(
            text = config.userName,
            leadIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    )
                    SubcomposeAsyncImage(
                        model = config.userImageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .clickable {
                                navController.navigate(
                                    MainNavItem.OtherProfile.screenRoute + "/${config.userId}"
                                )
                            },
                        contentScale = ContentScale.Crop,
                        loading = {
                            PhotoLoadingPlaceholder()
                        },
                        error = {
                            PhotoPlaceholder()
                        }
                    )
                }
            },
            trailIcon = {
                ReportsMenu(onReportClick = singleChatViewModel::onReportClick)
            }
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (isLoading) {
                LoadingView(backgroundColor = GreyLight)
            } else {
                ThemesView(
                    themes = themes,
                    onThemeClick = singleChatViewModel::sendMessage,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(top = 16.dp)
                )

                if (messages.isNotEmpty()) {
                    MessagesView(
                        messages = messages,
                        requestNext = {
                            singleChatViewModel.requestNext()
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Здесь пока нет сообщений",
                            textAlign = TextAlign.Center,
                            style = Typography.body1.copy(color = GreyDark),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }

        MessageInput(onSendClick = singleChatViewModel::sendMessage)
    }
}

@Composable
fun MessagesView(
    messages: List<MessageUiModel>,
    requestNext: () -> Unit
) {
    val state = rememberLazyListState()
    var prevSize by remember { mutableStateOf(messages.size) }

    LaunchedEffect(messages.size) {
        if (messages.size == prevSize + 1) {
            state.animateScrollToItem(0)
        }
        prevSize = messages.size
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        contentPadding = PaddingValues(horizontal = 16.dp),
        reverseLayout = true,
        state = state
    ) {
        itemsIndexed(
            items = messages,
            key = { _, message ->
                when (message) {
                    is MessageUiModel.SentMessageUiModel -> message.id
                    is MessageUiModel.StubMessageUiModel -> message.id
                }
            }
        ) { index, message ->
            LaunchedEffect(index) {
                if (messages.size - index == MESSAGES_TILL_END_TO_REQUEST) {
                    requestNext.invoke()
                }
            }
            MessageView(
                message = message,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }
}

@Composable
fun MessageInput(
    onSendClick: (String) -> Unit
) {
    var message by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White),
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            EditText(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp),
                hint = "Ваше сообщение",
                value = message,
                onValueChange = { message = it },
                onClearClicked = { message = "" },
                maxCharacters = 1000,
                height = Size.S,
                heightMax = Size.M,
                isSingleLine = false,
            )

            MainHeartIcon(
                modifier = Modifier
                    .rotate(-90f)
                    .clickable {
                        if (message.isNotEmpty()) {
                            onSendClick.invoke(message)
                            message = ""
                        }
                    },
                size = Size.S
            )
        }
    }
}

@ExperimentalPagerApi
@Preview
@Composable
fun SingleChatPreview() {
    val messages = listOf<MessageUiModel>(
        MessageUiModel.SentMessageUiModel(
            id = 0,
            text = "Hello",
            date = "20.08.2020",
            isSeen = true,
            isFromMe = true
        ),
        MessageUiModel.SentMessageUiModel(
            id = 1,
            text = "Hello",
            date = "20.08.2020",
            isSeen = false,
            isFromMe = false
        ),
    ).reversed()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GreyLight),
    ) {
        TopBar(
            text = "config.userName",
            leadIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                    }
                )
            },
            trailIcon = {
                SubcomposeAsyncImage(
                    model = "",
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
            }
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ThemesView(
                themes = listOf(ThemeUiModel("Сноуборд или лыжи?")),
                onThemeClick = {},
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            )

            if (messages.isNotEmpty()) {
                MessagesView(
                    messages = messages,
                    requestNext = {
                    }
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Здесь пока нет сообщений",
                        textAlign = TextAlign.Center,
                        style = Typography.body1.copy(color = GreyDark),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }

        MessageInput(onSendClick = {})
    }
}
