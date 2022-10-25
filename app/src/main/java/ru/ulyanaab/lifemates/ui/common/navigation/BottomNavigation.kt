package ru.ulyanaab.lifemates.ui.common.navigation

import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.ulyanaab.lifemates.ui.common.theme.GreyDark
import ru.ulyanaab.lifemates.ui.common.theme.PinkMain
import ru.ulyanaab.lifemates.ui.common.theme.Typography

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Profile,
        BottomNavItem.Feed,
        BottomNavItem.Chats,
    )
    androidx.compose.material.BottomNavigation(
        backgroundColor = Color.White,
        contentColor = GreyDark
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconResId),
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = Typography.caption.copy(fontSize = 9.sp)
                    )
                },
                selectedContentColor = PinkMain,
                unselectedContentColor = GreyDark,
                alwaysShowLabel = true,
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
