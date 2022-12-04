package ru.ulyanaab.lifemates.ui.common.navigation.auth

sealed class AuthNavItem(
    val screenRoute: String,
) {
    object Auth : AuthNavItem("auth")
    object RegisterFirst : AuthNavItem("register_first")
    object RegisterSecond : AuthNavItem("register_second")
    object Interests : AuthNavItem("interests")
}
