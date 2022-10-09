package ru.ulyanaab.lifemates.data.dto.request

import com.squareup.moshi.Json

data class LoginRequestDto(
    @Json(name = "email")
    val email: String,

    @Json(name = "password")
    val password: String,
)
