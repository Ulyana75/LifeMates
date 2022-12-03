package ru.ulyanaab.lifemates.data.dto.common

import com.squareup.moshi.Json

data class InterestDto(
    @Json(name = "id")
    val id: Int,

    @Json(name = "value")
    val value: String,
)
