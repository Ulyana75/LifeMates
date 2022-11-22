package ru.ulyanaab.lifemates.domain.user_info.model

import ru.ulyanaab.lifemates.domain.common.model.ContactModel
import ru.ulyanaab.lifemates.domain.common.model.GenderModel
import ru.ulyanaab.lifemates.domain.common.model.LocationModel

data class UserInfoGetModel(
    val id: Long,
    val email: String,
    val name: String?,
    val description: String?,
    val gender: GenderModel,
    val birthday: String?,
    val settings: UserSettingsModel,
    val interests: List<Int> = emptyList(),
    val imagesUrls: List<String>,
    val contacts: List<ContactModel>,
)

data class UserInfoUpdateModel(
    val name: String,
    val description: String?,
    val gender: GenderModel,
    val birthday: String?,
    val interests: List<Int> = emptyList(),
    val imagesUrls: List<String>,
    val location: LocationModel?,
    val settings: UserSettingsModel,
    val contacts: List<ContactModel>,
)
