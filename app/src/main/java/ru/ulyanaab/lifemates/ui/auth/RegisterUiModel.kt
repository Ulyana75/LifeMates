package ru.ulyanaab.lifemates.ui.auth

import android.graphics.Bitmap
import ru.ulyanaab.lifemates.domain.common.model.ContactModel
import ru.ulyanaab.lifemates.domain.common.model.GenderModel

data class RegisterUiModel(
    val email: String,
    val password: String,
    val name: String,
    val description: String,
    val gender: GenderModel,
    val birthday: String,
    // TODO val images: List<Bitmap>,
    val contacts: List<ContactModel>,
)
