package ru.ulyanaab.lifemates.ui.register

import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel

data class RegisterUiModel(
    val email: String,
    val password: String,
    val name: String,
    val age: String,
    val gender: RoundedBlockUiModel?,
    val showingGender: RoundedBlockUiModel?,
    val description: String,
    val telegram: String,
    val vk: String,
    val viber: String,
    val whatsapp: String,
    val instagram: String
)
