package ru.ulyanaab.lifemates.ui.auth

import ru.ulyanaab.lifemates.domain.auth.model.RegisterModel
import javax.inject.Inject

class RegisterMapper @Inject constructor() {

    fun mapToModel(uiModel: RegisterUiModel): RegisterModel {
        return RegisterModel(
            email = uiModel.email,
            password = uiModel.password,
            name = uiModel.name,
            description = uiModel.description.nullIfEmpty(),
            gender = uiModel.gender,
            birthday = uiModel.birthday,

        )
    }
}

private fun String.nullIfEmpty(): String? {
    return if (isEmpty()) null else this
}
