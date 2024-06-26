package ru.ulyanaab.lifemates.ui.match

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.common.navigation.main.MainNavItem
import ru.ulyanaab.lifemates.ui.common.widget.ArrowsView
import ru.ulyanaab.lifemates.ui.common.widget.BadgeNew
import ru.ulyanaab.lifemates.ui.common.widget.Button
import ru.ulyanaab.lifemates.ui.common.widget.CardOffset
import ru.ulyanaab.lifemates.ui.common.widget.LoadingView
import ru.ulyanaab.lifemates.ui.common.widget.OtherUserView
import ru.ulyanaab.lifemates.ui.common.widget.TopBar


@ExperimentalPagerApi
@Composable
fun MatchScreen(
    navController: NavController,
    matchViewModel: MatchViewModel
) {
    val isLoading by matchViewModel.isLoading.collectAsState()

    if (isLoading) {
        LoadingView(backgroundColor = Color.White)
    }
    MatchView(matchViewModel = matchViewModel, navController = navController)
}

@ExperimentalPagerApi
@Composable
fun MatchView(
    navController: NavController,
    matchViewModel: MatchViewModel
) {
    LaunchedEffect(Unit) {
        matchViewModel.attach()
    }
    val matches by matchViewModel.matchesStateFlow.collectAsState()
    val areMatchesFinished by matchViewModel.matchesAreFinishedFlow.collectAsState()
    val count = matches.size

    val pagerState = rememberPagerState()
    var prevPage by remember { mutableStateOf(0) }

    val scope = rememberCoroutineScope()

    Column {
        TopBar(
            text = "Ваши метчи",
            leadIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }
                )
            },
        )

        Box(modifier = Modifier.fillMaxSize()) {

            HorizontalPager(
                count = count,
                modifier = Modifier.fillMaxSize(),
                state = pagerState
            ) { page ->
                LaunchedEffect(page) {
                    if (prevPage < page && page == count - MatchViewModel.MATCHES_TILL_END_TO_REQUEST) {
                        matchViewModel.requestNext()
                    }
                    prevPage = page
                }
                val item = matches[page]
                MatchItem(
                    model = item,
                    onGoToChatClick = {
                        navController.navigate(
                            MainNavItem.SingleChat.screenRoute +
                                    "/${item.id}" +
                                    "/${item.user.id}" +
                                    "/${item.user.actualName}" +
                                    "?imageUrl=${item.user.imageUrl}"
                        )
                    }
                )
            }

            ArrowsView(
                isLeftArrowEnabled = pagerState.currentPage != 0,
                isRightArrowEnabled = !(pagerState.currentPage == count - 1
                        && (areMatchesFinished || count < MatchViewModel.MATCHES_REQUEST_COUNT)),
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
    }
}

@Composable
fun MatchItem(
    model: MatchUiModel,
    onGoToChatClick: () -> Unit,
) {
    Box {
        OtherUserView(
            model = model.user,
            cardOffset = CardOffset.M
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                onClick = {
                    onGoToChatClick.invoke()
                },
                text = "Начать общение!"
            )
        }

        if (!model.isSeen) {
            BadgeNew(Modifier.align(Alignment.TopStart).padding(8.dp))
        }
    }
}


@Preview
@Composable
fun ArrowsPreview() {
    ArrowsView(
        isLeftArrowEnabled = false,
        isRightArrowEnabled = true,
        onLeftArrowClick = {},
        onRightArrowClick = {}
    )
}
