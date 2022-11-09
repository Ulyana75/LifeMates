package ru.ulyanaab.lifemates.data.dto.response

import com.squareup.moshi.Json
import ru.ulyanaab.lifemates.data.dto.common.OtherUserDto

data class GetMatchesResponse(
    @Json(name = "matches")
    val matches: List<MatchDto>,

    @Json(name = "count")
    val count: Int,
)

data class MatchDto(
    @Json(name = "user")
    val user: OtherUserDto,

    @Json(name = "isSeen")
    val isSeen: Boolean,
)
