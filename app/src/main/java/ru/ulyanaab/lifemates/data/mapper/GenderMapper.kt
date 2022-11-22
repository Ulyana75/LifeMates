package ru.ulyanaab.lifemates.data.mapper

import ru.ulyanaab.lifemates.data.dto.common.GenderDto
import ru.ulyanaab.lifemates.domain.common.model.GenderModel
import javax.inject.Inject

class GenderMapper @Inject constructor() {

    fun mapToDto(model: GenderModel): GenderDto {
        return when (model) {
            GenderModel.MAN -> GenderDto.MAN
            GenderModel.WOMAN -> GenderDto.WOMAN
            GenderModel.NON_BINARY -> GenderDto.NON_BINARY
        }
    }

    fun mapToModel(dto: GenderDto): GenderModel {
        return when (dto) {
            GenderDto.MAN -> GenderModel.MAN
            GenderDto.WOMAN -> GenderModel.WOMAN
            GenderDto.NON_BINARY -> GenderModel.NON_BINARY
        }
    }
}
