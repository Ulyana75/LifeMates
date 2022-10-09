package ru.ulyanaab.lifemates.data.dto.response

import com.squareup.moshi.Json
import ru.ulyanaab.lifemates.data.dto.common.ContactDto

data class GetFeedResponseDto(
    @Json(name = "mates")
    val mates: List<MateDto>,
)

data class MateDto(
    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "gender")
    val gender: Int,

    @Json(name = "age")
    val age: Int,

    @Json(name = "distance")
    val distance: Int,

    @Json(name = "imageUrls")
    val imageUrls: List<String>,

    @Json(name = "contacts")
    val contacts: List<ContactDto>,

    // TODO interests
)
