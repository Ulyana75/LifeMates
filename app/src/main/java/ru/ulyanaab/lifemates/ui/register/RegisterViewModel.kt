package ru.ulyanaab.lifemates.ui.register

import ru.ulyanaab.lifemates.domain.auth.interactor.AuthInteractor
import ru.ulyanaab.lifemates.domain.auth.model.RegisterModel
import ru.ulyanaab.lifemates.domain.common.model.ContactModel
import ru.ulyanaab.lifemates.domain.common.model.ContactType
import ru.ulyanaab.lifemates.domain.common.model.GenderModel
import ru.ulyanaab.lifemates.domain.user_info.model.UserSettingsModel
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel
import ru.ulyanaab.lifemates.ui.common.utils.nullIfEmpty
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) {

    private var login = ""
    private var password = ""

    fun saveLoginAndPassword(login: String, password: String) {
        this.login = login
        this.password = password
    }

    fun getLoginAndPassword(): Pair<String, String> {
        return Pair(login, password)
    }

    fun onRegisterClick(
        name: String,
        age: String,
        gender: RoundedBlockUiModel?,
        showingGender: RoundedBlockUiModel?,
        description: String,
        telegram: String,
        vk: String,
        viber: String,
        whatsapp: String,
        instagram: String
    ) {

        authInteractor.register(
            RegisterModel(
                email = login,
                password = password,
                name = name,
                description = description.nullIfEmpty(),
                gender = mapGender(gender),
                birthday = age.nullIfEmpty(),
                imageUrls = emptyList(),
                location = null,
                settings = UserSettingsModel(mapGender(showingGender)),
                contacts = listOfNotNull(
                    telegram.nullIfEmpty()?.let { ContactModel(ContactType.TELEGRAM, it) },
                    vk.nullIfEmpty()?.let { ContactModel(ContactType.VK, it) },
                    viber.nullIfEmpty()?.let { ContactModel(ContactType.VIBER, it) },
                    whatsapp.nullIfEmpty()?.let { ContactModel(ContactType.WHATSAPP, it) },
                    instagram.nullIfEmpty()?.let { ContactModel(ContactType.INSTAGRAM, it) },
                )
            )
        )
    }

    // TODO map beautiful
    private fun mapGender(uiModel: RoundedBlockUiModel?): GenderModel {
        return when (uiModel?.text) {
            "Мужской" -> GenderModel.MAN
            "Женский" -> GenderModel.WOMAN
            else -> GenderModel.NON_BINARY
        }
    }
}
