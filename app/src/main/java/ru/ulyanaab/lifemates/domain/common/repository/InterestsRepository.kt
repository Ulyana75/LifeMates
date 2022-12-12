package ru.ulyanaab.lifemates.domain.common.repository

import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.common.model.InterestModel

interface InterestsRepository {
    suspend fun getInterests(): Result<List<InterestModel>>
}
