package ru.ulyanaab.lifemates.domain.auth.model

import ru.ulyanaab.lifemates.domain.common.model.ContactModel
import ru.ulyanaab.lifemates.domain.common.model.GenderModel
import ru.ulyanaab.lifemates.domain.common.model.LocationModel
import ru.ulyanaab.lifemates.domain.user_info.model.UserSettingsModel

data class RegisterModel(
    val email: String,
    val password: String,
    val name: String,
    val description: String?,
    val gender: GenderModel,
    val birthday: String?,
    val interests: List<Int>,
    val imageUrls: List<String>,
    val location: LocationModel?,
    val settings: UserSettingsModel,
    val contacts: List<ContactModel>,
)
