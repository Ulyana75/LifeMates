package ru.ulyanaab.lifemates.data.dto.common

import com.squareup.moshi.Json

data class LocationDto(
    @Json(name = "latitude")
    val latitude: Double,

    @Json(name = "longitude")
    val longitude: Double,
)
