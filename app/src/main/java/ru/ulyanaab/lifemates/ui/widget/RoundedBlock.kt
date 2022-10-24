package ru.ulyanaab.lifemates.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel
import ru.ulyanaab.lifemates.ui.theme.GreyLight
import ru.ulyanaab.lifemates.ui.theme.PinkMain
import ru.ulyanaab.lifemates.ui.theme.Shapes
import ru.ulyanaab.lifemates.ui.theme.Typography

@Composable
fun RoundedBlocksSingleChoice(
    onChoiceChanged: (RoundedBlockUiModel) -> Unit,
    elementsList: List<RoundedBlockUiModel>,
    modifier: Modifier = Modifier,
) {
    var selectedValue by remember {
        mutableStateOf(elementsList.find { it.isChosen })
    }

    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        elementsList.forEach {
            RoundedBlock(
                text = it.text,
                isChosen = selectedValue == it,
                modifier = Modifier.clickable {
                    selectedValue = it
                    onChoiceChanged.invoke(it)
                }
            )
        }
    }
}

@Composable
fun RoundedBlock(
    text: String,
    isChosen: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clip(Shapes.small)
            .background(
                color = if (isChosen) PinkMain else GreyLight
            )
            .then(modifier)
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .background(color = Color.Transparent),
            text = text,
            style = Typography.body1.copy(color = if (isChosen) Color.White else Color.Black)
        )
    }
}

@Preview
@Composable
fun RoundedBlockPreview() {
    Surface(Modifier.fillMaxSize()) {
        RoundedBlocksSingleChoice(
            onChoiceChanged = {},
            elementsList = listOf(
                RoundedBlockUiModel("Drive", true),
                RoundedBlockUiModel("Nightcall", false),
                RoundedBlockUiModel("Meme", false),
            )
        )
    }
}
