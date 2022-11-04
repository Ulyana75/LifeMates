package ru.ulyanaab.lifemates.domain.common.interactor

import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.common.repository.TokensRepository
import ru.ulyanaab.lifemates.domain.common.repository.TokensStorage
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthState
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthStateHolder
import javax.inject.Inject

class TokenInteractor @Inject constructor(
    private val tokensStorage: TokensStorage,
    private val tokensRepository: TokensRepository,
    private val authStateHolder: AuthStateHolder,
) {

    /**
     * returns true, if refreshing successful
     *
     * false if unsuccessful
     * **/
    suspend fun getAndSaveNewTokens(onSuccess: suspend () -> Unit = {}): Boolean {
        val currentTokens = tokensStorage.get()
        val newTokensResult = tokensRepository.refresh(currentTokens)

        if (newTokensResult is Result.Success) {
            tokensStorage.put(newTokensResult.data)
            onSuccess.invoke()
            return true
        }

        if (newTokensResult is Result.Failure && newTokensResult.error is Error.Forbidden) {
            authStateHolder.update(AuthState.UNAUTHORIZED)
        }

        return false
    }

    suspend fun revokeTokens() {
        val currentTokens = tokensStorage.get()
        tokensRepository.revoke(currentTokens)
        tokensStorage.clear()
    }
}
