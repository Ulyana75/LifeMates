package ru.ulyanaab.lifemates.ui.register

import ru.ulyanaab.lifemates.domain.auth.model.RegisterModel
import ru.ulyanaab.lifemates.domain.common.model.ContactModel
import ru.ulyanaab.lifemates.domain.common.model.ContactType
import ru.ulyanaab.lifemates.domain.common.model.GenderModel
import ru.ulyanaab.lifemates.domain.user_info.model.UserSettingsModel
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel
import ru.ulyanaab.lifemates.ui.common.utils.dateFormatterUniversal
import ru.ulyanaab.lifemates.ui.common.utils.dateFormatterUsual
import ru.ulyanaab.lifemates.ui.common.utils.nullIfEmpty
import java.time.LocalDate
import javax.inject.Inject

class RegisterMapper @Inject constructor() {

    fun mapToDomainModel(uiModel: RegisterUiModel): RegisterModel {
        return RegisterModel(
            email = uiModel.email,
            password = uiModel.password,
            name = uiModel.name,
            description = uiModel.description.nullIfEmpty()?.let {
                LocalDate.parse(it, dateFormatterUsual()).format(dateFormatterUniversal())
            },
            gender = mapGender(uiModel.gender),
            birthday = uiModel.birthday.nullIfEmpty(),
            imageUrls = listOfNotNull(uiModel.imageUrl),
            location = null,
            settings = UserSettingsModel(mapGender(uiModel.showingGender)),
            contacts = listOfNotNull(
                uiModel.telegram.nullIfEmpty()?.let { ContactModel(ContactType.TELEGRAM, it) },
                uiModel.vk.nullIfEmpty()?.let { ContactModel(ContactType.VK, it) },
                uiModel.viber.nullIfEmpty()?.let { ContactModel(ContactType.VIBER, it) },
                uiModel.whatsapp.nullIfEmpty()?.let { ContactModel(ContactType.WHATSAPP, it) },
                uiModel.instagram.nullIfEmpty()?.let { ContactModel(ContactType.INSTAGRAM, it) },
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
