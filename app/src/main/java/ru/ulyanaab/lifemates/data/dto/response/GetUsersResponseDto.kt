package ru.ulyanaab.lifemates.data.dto.response

import com.squareup.moshi.Json
import ru.ulyanaab.lifemates.data.dto.common.ContactDto
import ru.ulyanaab.lifemates.data.dto.common.GenderDto
import ru.ulyanaab.lifemates.data.dto.common.OtherUserDto

data class GetUsersResponseDto(
    @Json(name = "mates")
    val mates: List<OtherUserDto>,
)
