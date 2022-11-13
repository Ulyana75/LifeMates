package ru.ulyanaab.lifemates.ui.feed

import ru.ulyanaab.lifemates.domain.users.model.OtherUserModel
import ru.ulyanaab.lifemates.ui.common.mapper.ContactMapper
import ru.ulyanaab.lifemates.ui.common.mapper.GenderMapper
import ru.ulyanaab.lifemates.ui.common.model.OtherUserUiModel
import javax.inject.Inject

class OtherUserMapper @Inject constructor(
    private val genderMapper: GenderMapper,
    private val contactMapper: ContactMapper,
) {

    fun mapToUiModel(model: OtherUserModel): OtherUserUiModel {
        val title = if (model.age == null)
            model.name
        else
            "${model.name}, ${model.age}"

        val subtitle = if (model.distance == null)
            genderMapper.mapToTextAsDescription(model.gender)
        else
            "${genderMapper.mapToTextAsDescription(model.gender)}, ${model.distance} км от вас"

        return OtherUserUiModel(
            id = model.id,
            title = title,
            subtitle = subtitle,
            imageUrl = model.imagesUrls.firstOrNull(),
            description = model.description,
            contacts = model.contacts.map(contactMapper::mapToUiModel),
        )
    }

    fun mapToMatchUiModel(model: OtherUserUiModel): MatchUiModel {
        return MatchUiModel(
            title = "${model.title} тоже лайкнул(а) вас!",
            contacts = model.contacts,
            imageUrl = model.imageUrl
        )
    }
}
