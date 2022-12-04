package ru.ulyanaab.lifemates.ui.interests

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.common.widget.Button
import ru.ulyanaab.lifemates.ui.common.widget.EditText
import ru.ulyanaab.lifemates.ui.common.widget.LoadingView
import ru.ulyanaab.lifemates.ui.common.widget.RoundedBlockMultipleChoice
import ru.ulyanaab.lifemates.ui.common.widget.TopBar

@Composable
fun InterestsScreen(
    interestsViewModel: InterestsViewModel,
    navController: NavController,
) {
    LaunchedEffect(Unit) {
        interestsViewModel.attach()
    }

    val isLoading by interestsViewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {
        TopBar(
            text = "Выберите 3 интереса",
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

        if (isLoading) {
            LoadingView(
                backgroundColor = Color.White
            )
        } else {
            InterestsView(interestsViewModel, navController)
        }
    }
}

@Composable
fun ColumnScope.InterestsView(
    interestsViewModel: InterestsViewModel,
    navController: NavController,
) {
    var searchFilter by remember { mutableStateOf("") }

    val interests by remember { mutableStateOf(interestsViewModel.getAllInterests()) }
    var chosenInterests by remember { mutableStateOf(interestsViewModel.getChosenInterests()) }

    EditText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp, start = 16.dp, end = 16.dp, top = 16.dp),
        hint = "Поиск...",
        value = searchFilter,
        onValueChange = { searchFilter = it },
        onClearClicked = { searchFilter = "" }
    )

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .weight(weight = 1f, fill = false)
    ) {
        if (interests.isNotEmpty()) {
            RoundedBlockMultipleChoice(
                onChoiceChanged = { chosenInterests = it },
                elementsList = interests.filter {
                    it.text.lowercase().contains(searchFilter.lowercase())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                choiceLimit = 3,
            )
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = 4.dp,
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = {
                interestsViewModel.onDoneButtonClick(chosenInterests)
                navController.popBackStack()
            },
            text = "Готово"
        )
    }
}
