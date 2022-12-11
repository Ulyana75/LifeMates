package ru.ulyanaab.lifemates.ui.common.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel
import ru.ulyanaab.lifemates.ui.common.theme.GreyLight
import ru.ulyanaab.lifemates.ui.common.theme.PinkMain
import ru.ulyanaab.lifemates.ui.common.theme.Shapes
import ru.ulyanaab.lifemates.ui.common.theme.Typography
import ru.ulyanaab.lifemates.ui.common.utils.showToast

@Composable
fun RoundedBlocksSingleChoice(
    onChoiceChanged: (RoundedBlockUiModel) -> Unit,
    elementsList: List<RoundedBlockUiModel>,
    modifier: Modifier = Modifier,
) {
    var selectedValue by remember {
        mutableStateOf(elementsList.find { it.isChosen })
    }

    FlowRow(
        modifier = modifier,
        mainAxisSpacing = 5.dp,
        crossAxisSpacing = 5.dp,
    ){
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
fun RoundedBlocksWithoutChoice(
    elementsList: List<RoundedBlockUiModel>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        mainAxisSpacing = 5.dp,
        crossAxisSpacing = 5.dp,
    ) {
        elementsList.forEach {
            RoundedBlock(
                text = it.text,
                isChosen = it.isChosen,
            )
        }
    }
}

@Composable
fun RoundedBlockMultipleChoice(
    onChoiceChanged: (List<RoundedBlockUiModel>) -> Unit,
    elementsList: List<RoundedBlockUiModel>,
    modifier: Modifier = Modifier,
    choiceLimit: Int = Int.MAX_VALUE,
) {
    var selectedValues by remember {
        mutableStateOf(elementsList.filter { it.isChosen })
    }

    val context = LocalContext.current

    FlowRow(
        modifier = modifier,
        mainAxisSpacing = 5.dp,
        crossAxisSpacing = 5.dp,
    ) {
        elementsList.forEach {
            val isChosen = selectedValues.contains(it)

            RoundedBlock(
                text = it.text,
                isChosen = isChosen,
                modifier = Modifier.clickable {
                    if (isChosen) {
                        selectedValues = selectedValues - it
                        onChoiceChanged.invoke(selectedValues)
                    } else {
                        if (selectedValues.size < choiceLimit) {
                            selectedValues = selectedValues + it
                            onChoiceChanged.invoke(selectedValues)
                        } else {
                            showToast("Вы выбрали максимальное количество", context)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun RoundedBlock(
    text: String,
    isChosen: Boolean,
    modifier: Modifier = Modifier,
    chosenColor: Color = PinkMain,
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clip(Shapes.small)
            .background(
                color = if (isChosen) chosenColor else GreyLight
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
        RoundedBlocksWithoutChoice(
            elementsList = listOf(
                RoundedBlockUiModel("Drive", true),
                RoundedBlockUiModel("Nightcall", false),
                RoundedBlockUiModel("Meme", false),
                RoundedBlockUiModel("Drive", true),
                RoundedBlockUiModel("Nightcall asdasd asdasd asd asd asd asd asd asd asds asd ", false),
                RoundedBlockUiModel("Meme", false),
                RoundedBlockUiModel("Drive", true),
                RoundedBlockUiModel("Nightcall", false),
                RoundedBlockUiModel("Meme", false),
            )
        )
    }
}
