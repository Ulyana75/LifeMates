package ru.ulyanaab.lifemates.ui.common.navigation.main

sealed class MainNavItem(
    val screenRoute: String,
) {
    object MainScreen : MainNavItem("main")
    object Matches : MainNavItem("matches")
    object SingleChat : MainNavItem("single_chat")
}
