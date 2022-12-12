package ru.ulyanaab.lifemates.ui.interests

import ru.ulyanaab.lifemates.di.AppScope
import ru.ulyanaab.lifemates.domain.common.model.InterestModel
import javax.inject.Inject

@AppScope
class InterestsRepository @Inject constructor() {
    var chosenInterests: List<InterestModel> = emptyList()

    fun clear() {
        chosenInterests = emptyList()
    }
}
