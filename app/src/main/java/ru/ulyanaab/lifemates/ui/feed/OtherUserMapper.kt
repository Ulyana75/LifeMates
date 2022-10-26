package ru.ulyanaab.lifemates.ui.feed

import ru.ulyanaab.lifemates.domain.common.model.ContactModel
import ru.ulyanaab.lifemates.domain.common.model.ContactType
import ru.ulyanaab.lifemates.domain.common.model.GenderModel
import ru.ulyanaab.lifemates.domain.users.model.OtherUserModel
import ru.ulyanaab.lifemates.ui.common.model.ContactUiModel
import ru.ulyanaab.lifemates.ui.common.model.OtherUserUiModel
import javax.inject.Inject

class OtherUserMapper @Inject constructor() {

    fun mapToUiModel(model: OtherUserModel): OtherUserUiModel {
        val title = if (model.age == null)
            model.name
        else
            "${model.name}, ${model.age}"

        val subtitle = if (model.distance == null)
            mapGender(model.gender)
        else
            "${mapGender(model.gender)}, ${model.distance} км от вас"

        return OtherUserUiModel(
            title = title,
            subtitle = subtitle,
            imageUrl = model.imagesUrls.firstOrNull(),
            description = model.description
        )
    }

    fun mapToMatchUiModel(model: OtherUserModel): MatchUiModel {
        return MatchUiModel(
            title = "${model.name} тоже лайкнул(а) вас!",
            contacts = model.contacts.map(::mapContact),
            imageUrl = model.imagesUrls.firstOrNull()
        )
    }

    //TODO
    private fun mapGender(gender: GenderModel): String {
        return when (gender) {
            GenderModel.MAN -> "Мужчина"
            GenderModel.WOMAN -> "Женщина"
            else -> "Не бинарная персона"
        }
    }

    private fun mapContact(model: ContactModel): ContactUiModel {
        return ContactUiModel(
            name = when (model.type) {
                ContactType.TELEGRAM -> "Telegram"
                ContactType.VK -> "VK"
                ContactType.VIBER -> "Viber"
                ContactType.WHATSAPP -> "Whatsapp"
                ContactType.INSTAGRAM -> "Instagram"
            },
            value = model.value
        )
    }
}
