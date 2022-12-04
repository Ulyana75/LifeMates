package ru.ulyanaab.lifemates.ui.profile

import ru.ulyanaab.lifemates.domain.common.model.ContactModel
import ru.ulyanaab.lifemates.domain.common.model.ContactType
import ru.ulyanaab.lifemates.domain.user_info.model.UserInfoGetModel
import ru.ulyanaab.lifemates.domain.user_info.model.UserInfoUpdateModel
import ru.ulyanaab.lifemates.domain.user_info.model.UserSettingsModel
import ru.ulyanaab.lifemates.ui.common.utils.dateFormatterUniversal
import ru.ulyanaab.lifemates.ui.common.utils.dateFormatterUsual
import ru.ulyanaab.lifemates.ui.common.utils.dateFormatterWithTime
import ru.ulyanaab.lifemates.ui.common.utils.nullIfEmpty
import java.time.LocalDate
import javax.inject.Inject

class ProfileMapper @Inject constructor() {

    fun mapToUiModel(model: UserInfoGetModel): ProfileUiModel {
        return ProfileUiModel(
            name = model.name ?: "",
            description = model.description ?: "",
            gender = model.gender,
            birthday = model.birthday?.let {
              LocalDate.parse(it, dateFormatterWithTime()).format(dateFormatterUsual())
            } ?: "",
            interests = model.interests.map { it.id },
            showingGender = model.settings.showingGender,
            telegram = model.contacts.find {
                it.type == ContactType.TELEGRAM
            }?.value ?: "",
            vk = model.contacts.find {
                it.type == ContactType.VK
            }?.value ?: "",
            viber = model.contacts.find {
                it.type == ContactType.VIBER
            }?.value ?: "",
            whatsapp = model.contacts.find {
                it.type == ContactType.WHATSAPP
            }?.value ?: "",
            instagram = model.contacts.find {
                it.type == ContactType.INSTAGRAM
            }?.value ?: "",
            imageUrl = model.imagesUrls.firstOrNull()
        )
    }

    fun mapToUpdateModel(model: ProfileUiModel): UserInfoUpdateModel {
        return UserInfoUpdateModel(
            name = model.name,
            description = model.description.nullIfEmpty(),
            gender = model.gender,
            birthday = model.birthday.nullIfEmpty()?.let {
                LocalDate.parse(it, dateFormatterUsual()).format(dateFormatterUniversal())
            },
            interests = model.interests,
            imagesUrls = listOfNotNull(model.imageUrl),
            location = null,
            settings = UserSettingsModel(model.showingGender),
            contacts = listOfNotNull(
                model.telegram.nullIfEmpty()?.let { ContactModel(ContactType.TELEGRAM, it) },
                model.vk.nullIfEmpty()?.let { ContactModel(ContactType.VK, it) },
                model.viber.nullIfEmpty()?.let { ContactModel(ContactType.VIBER, it) },
                model.whatsapp.nullIfEmpty()?.let { ContactModel(ContactType.WHATSAPP, it) },
                model.instagram.nullIfEmpty()?.let { ContactModel(ContactType.INSTAGRAM, it) },
            )
        )
    }
}
