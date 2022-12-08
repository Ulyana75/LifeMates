package ru.ulyanaab.lifemates.domain.chats.model

data class ChatUserModel(
    val id: Long,
    val name: String,
    val imageUrls: List<String>
)
