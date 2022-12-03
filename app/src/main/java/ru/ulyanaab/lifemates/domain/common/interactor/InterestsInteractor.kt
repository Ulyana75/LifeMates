package ru.ulyanaab.lifemates.domain.common.interactor

import ru.ulyanaab.lifemates.domain.common.model.InterestModel
import ru.ulyanaab.lifemates.domain.common.repository.InterestsRepository
import ru.ulyanaab.lifemates.domain.common.utils.ResultProcessorWithTokensRefreshing
import javax.inject.Inject

class InterestsInteractor @Inject constructor(
    private val interestsRepository: InterestsRepository,
    private val resultProcessorWithTokensRefreshing: ResultProcessorWithTokensRefreshing,
) {
    suspend fun getInterests(): List<InterestModel> {
        return resultProcessorWithTokensRefreshing.proceedAndReturn(
            resultProducer = {
                interestsRepository.getInterests()
            },
            onTokensRefreshedSuccessfully = {
                getInterests()
            }
        ) ?: emptyList()
    }
}