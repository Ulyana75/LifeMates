package ru.ulyanaab.lifemates.data.mapper

import ru.ulyanaab.lifemates.data.dto.common.InterestDto
import ru.ulyanaab.lifemates.domain.common.model.InterestModel
import javax.inject.Inject

class InterestsMapper @Inject constructor() {

    fun mapToDto(model: InterestModel): InterestDto {
        return InterestDto(
            id = model.id,
            value = model.value,
        )
    }

    fun mapToModel(dto: InterestDto): InterestModel {
        return InterestModel(
            id = dto.id,
            value = dto.value,
        )
    }
}
