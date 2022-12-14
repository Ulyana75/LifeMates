package ru.ulyanaab.lifemates.data.dto.response

import com.squareup.moshi.Json
import ru.ulyanaab.lifemates.data.dto.common.ContactDto
import ru.ulyanaab.lifemates.data.dto.common.GenderDto
import ru.ulyanaab.lifemates.data.dto.common.InterestDto


data class MeResponseDto(
    @Json(name = "id")
    val id: Long,

    @Json(name = "email")
    val email: String,

    @Json(name = "name")
    val name: String?,

    @Json(name = "description")
    val description: String?,

    @Json(name = "gender")
    val gender: GenderDto,

    @Json(name = "birthday")
    val birthday: String?,

    @Json(name = "settings")
    val settings: UserSettingsDto,

    @Json(name = "interests")
    val interests: List<InterestDto>,

    @Json(name = "imagesUrls")
    val imagesUrls: List<String>,

    @Json(name = "contacts")
    val contacts: List<ContactDto>,
)

data class UserSettingsDto(
    @Json(name = "showingGender")
    val showingGenderDto: GenderDto,
)
