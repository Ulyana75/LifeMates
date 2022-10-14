package ru.ulyanaab.lifemates.domain.repository

import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.model.TokensModel

interface TokensRepository {
    suspend fun refresh(tokensModel: TokensModel): Result<TokensModel>
}
