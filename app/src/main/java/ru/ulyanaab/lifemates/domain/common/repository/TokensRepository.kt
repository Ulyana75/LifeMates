package ru.ulyanaab.lifemates.domain.common.repository

import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.common.model.TokensModel

interface TokensRepository {
    suspend fun refresh(tokensModel: TokensModel): Result<TokensModel>
}
