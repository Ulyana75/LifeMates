package ru.ulyanaab.lifemates.domain.interactors

import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.model.TokensModel
import ru.ulyanaab.lifemates.domain.repository.TokensRepository
import ru.ulyanaab.lifemates.domain.repository.TokensStorage
import javax.inject.Inject

class RefreshTokenInteractor @Inject constructor(
    private val tokensStorage: TokensStorage,
    private val tokensRepository: TokensRepository,
) {

    suspend fun getAndSaveNewTokens(): Result<TokensModel> {
        val currentTokens = tokensStorage.get()
        val newTokensResult = tokensRepository.refresh(currentTokens)

        if (newTokensResult is Result.Success) {
            tokensStorage.put(newTokensResult.data)
        }

        return newTokensResult
    }
}
