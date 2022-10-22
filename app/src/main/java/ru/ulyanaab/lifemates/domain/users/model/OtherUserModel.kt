package ru.ulyanaab.lifemates.domain.users.model

import ru.ulyanaab.lifemates.domain.common.model.ContactModel
import ru.ulyanaab.lifemates.domain.common.model.GenderModel

data class OtherUserModel(
    val id: Long,
    val name: String?,
    val description: String?,
    val gender: GenderModel,
    val age: Int?,
    val distance: Int?,
    val interests: List<Int>,
    val imagesUrls: List<String>,
    val contacts: List<ContactModel>,
)
