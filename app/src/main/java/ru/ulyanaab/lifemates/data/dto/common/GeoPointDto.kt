package ru.ulyanaab.lifemates.data.dto.common

import com.squareup.moshi.Json

data class GeoPointDto(
    @Json(name = "latitude")
    val latitude: Double,

    @Json(name = "longitude")
    val longitude: Double,
)
