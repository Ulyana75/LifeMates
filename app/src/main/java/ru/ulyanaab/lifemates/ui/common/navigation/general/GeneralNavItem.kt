package ru.ulyanaab.lifemates.ui.common.navigation.general

sealed class GeneralNavItem(
    val screenRoute: String,
) {
    object Loading : GeneralNavItem("loading")
    object MainOrAuth : GeneralNavItem("main_or_auth")
}
