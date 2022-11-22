package ru.ulyanaab.lifemates.data.dto.common

import com.squareup.moshi.Json

data class OtherUserDto(
    @Json(name = "id")
    val id: Long,

    @Json(name = "name")
    val name: String,

    @Json(name = "description")
    val description: String?,

    @Json(name = "gender")
    val gender: GenderDto,

    @Json(name = "age")
    val age: Int?,

    @Json(name = "distance")
    val distance: Int?,

    @Json(name = "interests")
    val interests: List<Int>,

    @Json(name = "imagesUrls")
    val imagesUrls: List<String>,

    @Json(name = "contacts")
    val contacts: List<ContactDto>,
)
