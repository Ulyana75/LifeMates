package ru.ulyanaab.lifemates.ui.chats

data class ChatUiModel(
    val id: Long,
    val userId: Long,
    val userName: String,
    val userImageUrl: String,
    val message: String,
    val unreadCount: Int,
)
