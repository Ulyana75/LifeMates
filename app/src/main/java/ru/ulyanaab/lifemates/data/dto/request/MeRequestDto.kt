package ru.ulyanaab.lifemates.data.dto.request

import com.squareup.moshi.Json
import ru.ulyanaab.lifemates.data.dto.common.ContactDto
import ru.ulyanaab.lifemates.data.dto.common.GenderDto
import ru.ulyanaab.lifemates.data.dto.common.LocationDto
import ru.ulyanaab.lifemates.data.dto.response.UserSettingsDto

data class MeRequestDto(
    @Json(name = "name")
    val name: String,

    @Json(name = "description")
    val description: String?,

    @Json(name = "gender")
    val gender: GenderDto,

    @Json(name = "birthday")
    val birthday: String?,

    @Json(name = "interests")
    val interests: List<Int>,

    @Json(name = "imagesUrls")
    val imagesUrls: List<String>,

    @Json(name = "location")
    val location: LocationDto?,

    @Json(name = "settings")
    val settings: UserSettingsDto,

    @Json(name = "contacts")
    val contacts: List<ContactDto>,
)
