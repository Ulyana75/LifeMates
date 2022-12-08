package ru.ulyanaab.lifemates.data.dto.response

import com.squareup.moshi.Json

data class GetChatsResponseDto(
    @Json(name = "chats")
    val chats: List<ChatDto>,
)

data class ChatDto(
    @Json(name = "id")
    val id: Long,

    @Json(name = "user")
    val user: ChatUserDto,

    @Json(name = "message")
    val message: ChatMessageDto,
)

data class ChatUserDto(
    @Json(name = "id")
    val id: Long,

    @Json(name = "name")
    val name: String,

    @Json(name = "imageUrls")
    val imageUrls: List<String>
)

data class ChatMessageDto(
    @Json(name = "content")
    val content: String?,

    @Json(name = "createdAt")
    val createdAt: String,

    @Json(name = "user")
    val user: ChatUserDto,

    @Json(name = "isSeen")
    val isSeen: Boolean,
)
