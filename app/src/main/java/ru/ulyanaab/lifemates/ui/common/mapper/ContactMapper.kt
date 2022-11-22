package ru.ulyanaab.lifemates.ui.common.mapper

import ru.ulyanaab.lifemates.domain.common.model.ContactModel
import ru.ulyanaab.lifemates.domain.common.model.ContactType
import ru.ulyanaab.lifemates.ui.common.model.ContactUiModel
import javax.inject.Inject

class ContactMapper @Inject constructor() {

    fun mapToUiModel(model: ContactModel): ContactUiModel {
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
