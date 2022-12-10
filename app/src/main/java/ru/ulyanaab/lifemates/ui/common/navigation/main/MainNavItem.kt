package ru.ulyanaab.lifemates.ui.common.navigation.main

sealed class MainNavItem(
    val screenRoute: String,
) {
    object MainScreen : MainNavItem("main")
    object Matches : MainNavItem("matches")
    object SingleChat : MainNavItem("single_chat")
    object Interests : MainNavItem("interests")

    companion object {
        const val CHAT_ID_ARGUMENT = "chat_id"
        const val USER_ID_ARGUMENT = "user_id"
        const val USER_IMAGE_URL_ARGUMENT = "user_image_url"
        const val USER_NAME_ARGUMENT = "user_name"
    }
}
