package ru.ulyanaab.lifemates.ui.profile

import ru.ulyanaab.lifemates.domain.common.model.GenderModel

data class ProfileUiModel(
    val name: String,
    val description: String,
    val gender: GenderModel,
    val birthday: String,
    val showingGender: GenderModel,
    val telegram: String,
    val vk: String,
    val viber: String,
    val whatsapp: String,
    val instagram: String,
)
