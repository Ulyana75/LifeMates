package ru.ulyanaab.lifemates.data.dto.response

import com.squareup.moshi.Json

data class LikeResponseDto(
    @Json(name = "isItMutual")
    val isMatch: Boolean,

    @Json(name = "chatId")
    val chatId: Long?,
)
