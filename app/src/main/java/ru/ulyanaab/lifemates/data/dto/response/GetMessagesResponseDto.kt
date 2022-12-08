package ru.ulyanaab.lifemates.data.dto.response

import com.squareup.moshi.Json

data class GetMessagesResponseDto(
    @Json(name = "messages")
    val messages: List<ChatMessageDto>,
)

data class MessageDto(
    @Json(name = "id")
    val id: Long,

    @Json(name = "content")
    val content: String?,

    @Json(name = "createdAt")
    val createdAt: String,

    @Json(name = "userId")
    val userId: Long,

    @Json(name = "isSeen")
    val isSeen: Boolean,
)
