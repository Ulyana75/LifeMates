package ru.ulyanaab.lifemates.ui.single_chat

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.common.theme.GreyDark
import ru.ulyanaab.lifemates.ui.common.theme.GreyLight
import ru.ulyanaab.lifemates.ui.common.theme.Typography
import ru.ulyanaab.lifemates.ui.common.widget.EditText
import ru.ulyanaab.lifemates.ui.common.widget.LoadingView
import ru.ulyanaab.lifemates.ui.common.widget.MainHeartIcon
import ru.ulyanaab.lifemates.ui.common.widget.Size
import ru.ulyanaab.lifemates.ui.common.widget.TopBar

@Composable
fun SingleChatScreen(
    singleChatViewModel: SingleChatViewModel,
    navController: NavController,
    config: SingleChatScreenConfig,
) {
    DisposableEffect(Unit) {
        singleChatViewModel.attach()

        onDispose {
            singleChatViewModel.detach()
        }
    }

    val messages by singleChatViewModel.messagesStateFlow.collectAsState()
    val isLoading by singleChatViewModel.isLoading.collectAsState()
    val messagesAreFinished by singleChatViewModel.messagesAreFinishedFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GreyLight),
    ) {
        TopBar(
            text = config.userName,
            leadIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }
                )
            }, // TODO trailIcon
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (isLoading) {
                LoadingView(backgroundColor = GreyLight)
            } else {
                if (messages.isNotEmpty()) {
                    MessagesView(
                        messages = messages,
                        messagesAreFinished = messagesAreFinished
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
    messagesAreFinished: Boolean,
) {
    val state = rememberLazyListState()

    LaunchedEffect(messages.size) {
        state.animateScrollToItem(0)
    }

    LaunchedEffect(state) {
        snapshotFlow { state.firstVisibleItemIndex }
            .collect {
                Log.d("LOL", it.toString())
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        contentPadding = PaddingValues(horizontal = 16.dp),
        reverseLayout = true,
        state = state
    ) {
        items(
            items = messages,
            key = { message ->
                when (message) {
                    is MessageUiModel.SentMessageUiModel -> message.id
                    is MessageUiModel.StubMessageUiModel -> message.id
                }
            }
        ) { message ->
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

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

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
            }, // TODO trailIcon
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (messages.isNotEmpty()) {
                MessagesView(messages = messages, false)
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
