package ru.ulyanaab.lifemates.data.mapper

import ru.ulyanaab.lifemates.data.dto.common.OtherUserDto
import ru.ulyanaab.lifemates.data.dto.response.GetUsersResponseDto
import ru.ulyanaab.lifemates.domain.users.model.FeedModel
import ru.ulyanaab.lifemates.domain.users.model.OtherUserModel
import javax.inject.Inject

class UsersMapper @Inject constructor(
    private val contactMapper: ContactMapper,
    private val genderMapper: GenderMapper,
) {

    fun mapToFeedModel(dto: GetUsersResponseDto): FeedModel {
        return FeedModel(
            users = dto.mates.map(::mapToOtherUserModel)
        )
    }

    fun mapToOtherUserModel(dto: OtherUserDto): OtherUserModel {
        return OtherUserModel(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            gender = genderMapper.mapToModel(dto.gender),
            age = dto.age,
            distance = dto.distance,
            interests = dto.interests,
            imagesUrls = dto.imagesUrls,
            contacts = dto.contacts.map(contactMapper::mapToModel)
        )
    }
}
