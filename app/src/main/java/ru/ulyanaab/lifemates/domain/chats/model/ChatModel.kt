package ru.ulyanaab.lifemates.domain.chats.model

data class ChatModel(
    val id: Long,
    val user: ChatUserModel,
    val message: ChatMessageModel,
)
