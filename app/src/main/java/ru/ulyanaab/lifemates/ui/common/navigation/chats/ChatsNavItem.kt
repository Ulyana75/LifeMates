package ru.ulyanaab.lifemates.ui.common.navigation.chats

sealed class ChatsNavItem(
    val screenRoute: String
) {
    object Chats : ChatsNavItem("chats")
    object SingleChat : ChatsNavItem("single_chat")
    object Matches : ChatsNavItem("matches")
}
