package ru.ulyanaab.lifemates.data.mapper

import ru.ulyanaab.lifemates.data.dto.common.ContactDto
import ru.ulyanaab.lifemates.data.dto.common.ContactTypeDto
import ru.ulyanaab.lifemates.domain.common.model.ContactModel
import ru.ulyanaab.lifemates.domain.common.model.ContactType
import javax.inject.Inject

class ContactMapper @Inject constructor() {

    fun mapToDto(model: ContactModel): ContactDto {
        return ContactDto(
            type = when (model.type) {
                ContactType.TELEGRAM -> ContactTypeDto.TELEGRAM
                ContactType.VK -> ContactTypeDto.VK
                ContactType.VIBER -> ContactTypeDto.VIBER
                ContactType.WHATSAPP -> ContactTypeDto.WHATSAPP
                ContactType.INSTAGRAM -> ContactTypeDto.INSTAGRAM
            },
            value = model.value
        )
    }

    fun mapToModel(dto: ContactDto): ContactModel {
        return ContactModel(
            type = when (dto.type) {
                ContactTypeDto.TELEGRAM -> ContactType.TELEGRAM
                ContactTypeDto.VK -> ContactType.VK
                ContactTypeDto.VIBER -> ContactType.VIBER
                ContactTypeDto.WHATSAPP -> ContactType.WHATSAPP
                ContactTypeDto.INSTAGRAM -> ContactType.INSTAGRAM
            },
            value = dto.value,
        )
    }
}
