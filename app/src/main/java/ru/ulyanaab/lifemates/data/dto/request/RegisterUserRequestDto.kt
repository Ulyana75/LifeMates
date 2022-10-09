package ru.ulyanaab.lifemates.data.dto.request

import com.squareup.moshi.Json
import ru.ulyanaab.lifemates.data.dto.common.ContactDto
import ru.ulyanaab.lifemates.data.dto.common.GeoPointDto
import java.util.Date

data class RegisterUserRequestDto(
    @Json(name = "email")
    val email: String,

    @Json(name = "password")
    val password: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "description")
    val description: String?,

    @Json(name = "gender")
    val gender: Int,

    @Json(name = "birthday")
    val birthday: Date,

    // TODO interests

    @Json(name = "imageUrls")
    val imageUrls: List<String>,

    @Json(name = "location")
    val location: GeoPointDto,

    @Json(name = "contacts")
    val contacts: List<ContactDto>,
)
