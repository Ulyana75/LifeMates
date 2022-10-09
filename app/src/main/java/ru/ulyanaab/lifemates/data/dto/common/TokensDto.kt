package ru.ulyanaab.lifemates.data.dto.common

import com.squareup.moshi.Json

data class TokensDto(
    @Json(name = "accessToken")
    val accessToken: String,

    @Json(name = "refreshToken")
    val refreshToken: String,
)
