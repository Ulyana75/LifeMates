package ru.ulyanaab.lifemates.data.mapper

import ru.ulyanaab.lifemates.data.dto.request.RegisterUserRequestDto
import ru.ulyanaab.lifemates.data.dto.response.UserSettingsDto
import ru.ulyanaab.lifemates.domain.auth.model.RegisterModel
import ru.ulyanaab.lifemates.domain.user_info.model.UserSettingsModel
import javax.inject.Inject

class RegisterMapper @Inject constructor(
    private val contactMapper: ContactMapper,
    private val genderMapper: GenderMapper,
    private val locationMapper: LocationMapper,
) {

    fun map(model: RegisterModel): RegisterUserRequestDto {
        return RegisterUserRequestDto(
            email = model.email,
            password = model.password,
            name = model.name,
            description = model.description,
            gender = genderMapper.mapToDto(model.gender),
            birthday = model.birthday,
            interests = model.interests,
            imageUrls = model.imageUrls,
            location = model.location?.let(locationMapper::mapToDto),
            settings = map(model.settings),
            contacts = model.contacts.map(contactMapper::mapToDto),
        )
    }

    private fun map(settings: UserSettingsModel): UserSettingsDto {
        return UserSettingsDto(
            showingGenderDto = genderMapper.mapToDto(settings.showingGender)
        )
    }
}
