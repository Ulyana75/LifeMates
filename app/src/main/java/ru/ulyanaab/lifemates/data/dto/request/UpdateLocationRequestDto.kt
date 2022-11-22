package ru.ulyanaab.lifemates.data.dto.request

import com.squareup.moshi.Json
import ru.ulyanaab.lifemates.data.dto.common.LocationDto

data class UpdateLocationRequestDto(
    @Json(name = "location")
    val location: LocationDto?,
)
