package ru.ulyanaab.lifemates.ui.other_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import ru.ulyanaab.lifemates.R
import ru.ulyanaab.lifemates.ui.common.widget.CardOffset
import ru.ulyanaab.lifemates.ui.common.widget.LoadingView
import ru.ulyanaab.lifemates.ui.common.widget.OtherUserView
import ru.ulyanaab.lifemates.ui.common.widget.TopBar

@Composable
fun OtherProfileScreen(
    otherProfileViewModel: OtherProfileViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        otherProfileViewModel.attach()
    }

    val isLoading by otherProfileViewModel.isLoading.collectAsState()
    val user by otherProfileViewModel.userStateFlow.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        TopBar(
            leadIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }
                )
            }
        )

        if (isLoading) {
            LoadingView(backgroundColor = Color.White)
        } else {
            user?.let {
                OtherUserView(
                    model = it,
                    cardOffset = CardOffset.S,
                    onReportClick = otherProfileViewModel::onReportClick
                )
            }
        }
    }
}
