package ru.ulyanaab.lifemates.domain.match.interactor

import ru.ulyanaab.lifemates.domain.common.utils.ResultProcessorWithTokensRefreshing
import ru.ulyanaab.lifemates.domain.match.repository.MatchRepository
import ru.ulyanaab.lifemates.domain.users.model.OtherUserModel
import javax.inject.Inject

class MatchInteractor @Inject constructor(
    private val matchRepository: MatchRepository,
    private val resultProcessorWithTokensRefreshing: ResultProcessorWithTokensRefreshing,
) {

    suspend fun getMatches(offset: Int, limit: Int): List<OtherUserModel>? {
        return resultProcessorWithTokensRefreshing.proceedAndReturn(
            resultProducer = {
                matchRepository.getMatches(offset, limit)
            },
            onTokensRefreshedSuccessfully = {
                getMatches(offset, limit)
            }
        )
    }
}
