package ru.ulyanaab.lifemates.ui.register

import ru.ulyanaab.lifemates.domain.auth.model.RegisterModel
import ru.ulyanaab.lifemates.domain.common.model.ContactModel
import ru.ulyanaab.lifemates.domain.common.model.ContactType
import ru.ulyanaab.lifemates.domain.user_info.model.UserSettingsModel
import ru.ulyanaab.lifemates.ui.common.mapper.GenderMapper
import ru.ulyanaab.lifemates.ui.common.utils.dateFormatterUniversal
import ru.ulyanaab.lifemates.ui.common.utils.dateFormatterUsual
import ru.ulyanaab.lifemates.ui.common.utils.nullIfEmpty
import java.time.LocalDate
import javax.inject.Inject

class RegisterMapper @Inject constructor(
    private val genderMapper: GenderMapper
) {

    fun mapToDomainModel(uiModel: RegisterUiModel): RegisterModel {
        return RegisterModel(
            email = uiModel.email.trim(),
            password = uiModel.password,
            name = uiModel.name,
            description = uiModel.description.nullIfEmpty(),
            gender = genderMapper.mapToModel(uiModel.gender),
            birthday = uiModel.birthday.nullIfEmpty()?.let {
                LocalDate.parse(it, dateFormatterUsual()).format(dateFormatterUniversal())
            },
            imageUrls = listOfNotNull(uiModel.imageUrl),
            location = null,
            settings = UserSettingsModel(genderMapper.mapToModel(uiModel.showingGender)),
            contacts = listOfNotNull(
                uiModel.telegram.nullIfEmpty()?.let { ContactModel(ContactType.TELEGRAM, it) },
                uiModel.vk.nullIfEmpty()?.let { ContactModel(ContactType.VK, it) },
                uiModel.viber.nullIfEmpty()?.let { ContactModel(ContactType.VIBER, it) },
                uiModel.whatsapp.nullIfEmpty()?.let { ContactModel(ContactType.WHATSAPP, it) },
                uiModel.instagram.nullIfEmpty()?.let { ContactModel(ContactType.INSTAGRAM, it) },
            )
        )
    }
}
