package ru.ulyanaab.lifemates.data.mapper

import ru.ulyanaab.lifemates.data.dto.request.MeRequestDto
import ru.ulyanaab.lifemates.data.dto.response.MeResponseDto
import ru.ulyanaab.lifemates.data.dto.response.UserSettingsDto
import ru.ulyanaab.lifemates.domain.user_info.model.UserInfoGetModel
import ru.ulyanaab.lifemates.domain.user_info.model.UserInfoUpdateModel
import ru.ulyanaab.lifemates.domain.user_info.model.UserSettingsModel
import javax.inject.Inject

class MeMapper @Inject constructor(
    private val contactMapper: ContactMapper,
    private val genderMapper: GenderMapper,
    private val locationMapper: LocationMapper,
) {

    fun mapToDto(model: UserInfoUpdateModel): MeRequestDto {
        return MeRequestDto(
            name = model.name,
            description = model.description,
            gender = genderMapper.mapToDto(model.gender),
            birthday = model.birthday,
            interests = model.interests,
            imagesUrls = model.imagesUrls,
            location = model.location?.let(locationMapper::mapToDto),
            settings = map(model.settings),
            contacts = model.contacts.map(contactMapper::mapToDto)
        )
    }

    fun mapToModel(dto: MeResponseDto): UserInfoGetModel {
        return UserInfoGetModel(
            id = dto.id,
            email = dto.email,
            name = dto.name,
            description = dto.description,
            gender = genderMapper.mapToModel(dto.gender),
            birthday = dto.birthday,
            settings = map(dto.settings),
            interests = dto.interests,
            imagesUrls = dto.imagesUrls,
            contacts = dto.contacts.map(contactMapper::mapToModel)
        )
    }

    private fun map(settings: UserSettingsModel): UserSettingsDto {
        return UserSettingsDto(
            showingGenderDto = genderMapper.mapToDto(settings.showingGender)
        )
    }

    private fun map(settings: UserSettingsDto): UserSettingsModel {
        return UserSettingsModel(
            showingGender = genderMapper.mapToModel(settings.showingGenderDto)
        )
    }
}
