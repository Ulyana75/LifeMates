package ru.ulyanaab.lifemates.data.dto.common

import com.squareup.moshi.Json

enum class GenderDto {
    @Json(name = "Man")
    MAN,

    @Json(name = "Woman")
    WOMAN,

    @Json(name = "NonBinary")
    NON_BINARY
}
