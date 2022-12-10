package ru.ulyanaab.lifemates.ui.single_chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.common.theme.GreyLight
import ru.ulyanaab.lifemates.ui.common.widget.EditText
import ru.ulyanaab.lifemates.ui.common.widget.MainHeartIcon
import ru.ulyanaab.lifemates.ui.common.widget.Size
import ru.ulyanaab.lifemates.ui.common.widget.TopBar

@Composable
fun SingleChatScreen(
    singleChatViewModel: SingleChatViewModel,
    navController: NavController,
    config: SingleChatScreenConfig,
) {
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

        }

        MessageInput(onSendClick = singleChatViewModel::sendMessage)
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
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 34.dp),
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
                        }
                    },
                size = Size.S
            )
        }
    }
}

@Preview
@Composable
fun SingleChatPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GreyLight),
    ) {
        TopBar(
            text = "Ryan",
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

        }

        MessageInput(onSendClick = {})
    }
}
