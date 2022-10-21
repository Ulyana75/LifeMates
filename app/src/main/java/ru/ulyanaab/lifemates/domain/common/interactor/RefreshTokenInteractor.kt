package ru.ulyanaab.lifemates.domain.common.interactor

import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.common.model.TokensModel
import ru.ulyanaab.lifemates.domain.common.repository.TokensRepository
import ru.ulyanaab.lifemates.domain.common.repository.TokensStorage
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthEvent
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthorizationStateHolder
import javax.inject.Inject

class RefreshTokenInteractor @Inject constructor(
    private val tokensStorage: TokensStorage,
    private val tokensRepository: TokensRepository,
    private val authorizationStateHolder: AuthorizationStateHolder,
) {

    /**
     * returns true, if refreshing successful
     *
     * false if unsuccessful
     * **/
    suspend fun getAndSaveNewTokens(onSuccess: () -> Unit): Boolean {
        val currentTokens = tokensStorage.get()
        val newTokensResult = tokensRepository.refresh(currentTokens)

        if (newTokensResult is Result.Success) {
            tokensStorage.put(newTokensResult.data)
            onSuccess.invoke()
            return true
        }

        if (newTokensResult is Result.Failure && newTokensResult.error is Error.Unauthorized) {
            authorizationStateHolder.update(AuthEvent.UNAUTHORIZED)
        }

        return false
    }
}
