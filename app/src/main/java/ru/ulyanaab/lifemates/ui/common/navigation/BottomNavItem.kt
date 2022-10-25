package ru.ulyanaab.lifemates.ui.common.navigation

import ru.ulyanaab.lifemates.R

sealed class BottomNavItem(
    val title: String,
    val iconResId: Int,
    val screenRoute: String,
) {
    object Profile : BottomNavItem("Профиль", R.drawable.ic_profile, "profile")
    object Feed : BottomNavItem("Лента", R.drawable.ic_heart, "feed")
    object Chats : BottomNavItem("Чаты", R.drawable.ic_chat, "chats")
}
