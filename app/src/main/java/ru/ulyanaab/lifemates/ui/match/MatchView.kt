package ru.ulyanaab.lifemates.ui.match

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import ru.ulyanaab.lifemates.ui.common.model.OtherUserUiModel
import ru.ulyanaab.lifemates.ui.common.widget.Button
import ru.ulyanaab.lifemates.ui.common.widget.ContactsDialog
import ru.ulyanaab.lifemates.ui.common.widget.LoadingView
import ru.ulyanaab.lifemates.ui.common.widget.OtherUserView


@ExperimentalPagerApi
@Composable
fun MatchScreen(matchViewModel: MatchViewModel) {
    val isLoading by matchViewModel.isLoading.collectAsState()

    if (isLoading) {
        LoadingView(backgroundColor = Color.White)
    }
    MatchView(matchViewModel = matchViewModel)
}

@ExperimentalPagerApi
@Composable
fun MatchView(matchViewModel: MatchViewModel) {
    LaunchedEffect(Unit) {
        matchViewModel.attach()
    }
    val matches by matchViewModel.matchesStateFlow.collectAsState()
    val count = matches.size

    val pagerState = rememberPagerState()

    HorizontalPager(
        count = count,
        modifier = Modifier.fillMaxSize(),
        state = pagerState
    ) { page ->
        LaunchedEffect(page) {
            if (page == count - MatchViewModel.MATCHES_TILL_END_TO_REQUEST) {
                matchViewModel.requestNext()
            }
        }
        MatchItem(model = matches[page])
    }
}

@Composable
fun MatchItem(
    model: OtherUserUiModel
) {
    val openContactsDialog = remember { mutableStateOf(false) }

    ContactsDialog(
        openDialog = openContactsDialog,
        contacts = model.contacts
    )

    OtherUserView(model = model) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = {
                openContactsDialog.value = true
            },
            text = "Начать общение!"
        )
    }
}
