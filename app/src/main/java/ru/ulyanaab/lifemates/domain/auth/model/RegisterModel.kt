package ru.ulyanaab.lifemates.domain.auth.model

import ru.ulyanaab.lifemates.domain.common.model.ContactModel
import ru.ulyanaab.lifemates.domain.common.model.GenderModel
import ru.ulyanaab.lifemates.domain.common.model.LocationModel

data class RegisterModel(
    val email: String,
    val password: String,
    val name: String,
    val description: String?,
    val gender: GenderModel,
    val birthday: String,
    val interests: List<Int> = emptyList(),
    val imageUrls: List<String>,
    val location: LocationModel?,
    val contacts: List<ContactModel>,
)
