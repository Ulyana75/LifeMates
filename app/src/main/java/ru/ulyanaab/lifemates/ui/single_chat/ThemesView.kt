package ru.ulyanaab.lifemates.ui.single_chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.ui.common.theme.PurpleMain
import ru.ulyanaab.lifemates.ui.common.theme.Shapes
import ru.ulyanaab.lifemates.ui.common.theme.Typography
import ru.ulyanaab.lifemates.ui.common.widget.ArrowsView
import ru.ulyanaab.lifemates.ui.common.widget.RoundedBlock

@ExperimentalPagerApi
@Composable
fun ThemesView(
    themes: List<ThemeUiModel>,
    onThemeClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val count = themes.size

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = 6.dp,
        backgroundColor = Color.White,
        shape = Shapes.large,
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box {
                Text(
                    text = "Тема для разговора",
                    style = Typography.body1.copy(color = Color.Black),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                )

                ArrowsView(
                    isLeftArrowEnabled = pagerState.currentPage != 0,
                    isRightArrowEnabled = pagerState.currentPage != count - 1,
                    onLeftArrowClick = {
                        scope.launch {
                            if (pagerState.currentPage != 0) {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    },
                    onRightArrowClick = {
                        scope.launch {
                            if (pagerState.currentPage != count) {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            HorizontalPager(
                count = count,
                modifier = Modifier.fillMaxWidth(),
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                itemSpacing = 16.dp
            ) { page ->
                val theme = themes[page]
                RoundedBlock(
                    text = theme.value,
                    isChosen = true,
                    modifier = Modifier.clickable {
                        onThemeClick.invoke(theme.value)
                    },
                    chosenColor = PurpleMain,
                )
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
            )
        }
    }
}

@ExperimentalPagerApi
@Preview
@Composable
fun ThemesPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        ThemesView(
            modifier = Modifier.padding(16.dp),
            themes = listOf(
                ThemeUiModel("Лыжи или сноуборд? Лыжи или сноуборд?"),
            ),
            onThemeClick = {}
        )
    }
}
