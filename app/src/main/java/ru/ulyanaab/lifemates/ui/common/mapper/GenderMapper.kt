package ru.ulyanaab.lifemates.ui.common.mapper

import ru.ulyanaab.lifemates.domain.common.model.GenderModel
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel
import javax.inject.Inject

class GenderMapper @Inject constructor() {

    fun mapToModel(uiModel: RoundedBlockUiModel?): GenderModel {
        return when (uiModel?.text) {
            MAN -> GenderModel.MAN
            WOMAN -> GenderModel.WOMAN
            else -> GenderModel.NON_BINARY
        }
    }

    fun mapToText(model: GenderModel): String {
        return when (model) {
            GenderModel.MAN -> MAN
            GenderModel.WOMAN -> WOMAN
            GenderModel.NON_BINARY -> NON_BINARY
        }
    }

    fun mapToTextAsDescription(model: GenderModel): String {
        return when (model) {
            GenderModel.MAN -> MAN_DESCRIPTION
            GenderModel.WOMAN -> WOMAN_DESCRIPTION
            GenderModel.NON_BINARY -> NON_BINARY_DESCRIPTION
        }
    }

    companion object {
        const val MAN = "Мужской"
        const val WOMAN = "Женский"
        const val NON_BINARY = "Небинарный"

        const val MAN_DESCRIPTION = "Мужчина"
        const val WOMAN_DESCRIPTION = "Женщина"
        const val NON_BINARY_DESCRIPTION = "Небинарная персона"
    }
}
