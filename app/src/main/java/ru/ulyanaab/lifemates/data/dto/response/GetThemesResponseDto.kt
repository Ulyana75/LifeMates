package ru.ulyanaab.lifemates.data.dto.response

import com.squareup.moshi.Json

data class GetThemesResponseDto(
    @Json(name = "themes")
    val themes: List<ThemeDto>
)

data class ThemeDto(
    @Json(name = "id")
    val id: Long,

    @Json(name = "value")
    val value: String,
)
