package ru.ulyanaab.lifemates.ui.register

import ru.ulyanaab.lifemates.domain.common.model.InterestModel
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel

data class RegisterUiModel(
    val email: String,
    val password: String,
    val name: String,
    val birthday: String,
    val gender: RoundedBlockUiModel?,
    val showingGender: RoundedBlockUiModel?,
    val description: String,
    val imageUrl: String?,
    val interests: List<InterestModel>,
)
