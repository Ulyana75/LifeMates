package ru.ulyanaab.lifemates.data.dto.request

import com.squareup.moshi.Json

data class ReportRequestDto(
    @Json(name = "userId")
    val userId: Long,

    @Json(name = "type")
    val type: ReportTypeDto,
)

enum class ReportTypeDto {
    @Json(name = "Aggressiveness")
    AGGRESSIVENESS,

    @Json(name = "Harassment")
    HARASSMENT,

    @Json(name = "ProfileCheating")
    PROFILE_CHEATING
}
