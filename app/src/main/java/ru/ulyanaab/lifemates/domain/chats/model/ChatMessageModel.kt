package ru.ulyanaab.lifemates.domain.chats.model

data class ChatMessageModel(
    val id: Long,
    val content: String?,
    val createdAt: String,
    val userId: Long,
    val isSeen: Boolean,
)
