package ru.ulyanaab.lifemates.data.mapper

import ru.ulyanaab.lifemates.data.dto.common.LocationDto
import ru.ulyanaab.lifemates.domain.common.model.LocationModel
import javax.inject.Inject

class LocationMapper @Inject constructor() {

    fun mapToDto(model: LocationModel): LocationDto {
        return LocationDto(
            latitude = model.latitude,
            longitude = model.longitude,
        )
    }

    fun mapToModel(dto: LocationDto): LocationModel {
        return LocationModel(
            latitude = dto.latitude,
            longitude = dto.longitude,
        )
    }
}
