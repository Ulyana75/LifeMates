package ru.ulyanaab.lifemates.data.repositoryimpl

import retrofit2.awaitResponse
import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.data.api.TokenApi
import ru.ulyanaab.lifemates.data.dto.common.toTokensDto
import ru.ulyanaab.lifemates.data.dto.common.toTokensModel
import ru.ulyanaab.lifemates.domain.model.TokensModel
import ru.ulyanaab.lifemates.domain.repository.TokensRepository
import javax.inject.Inject

class TokensRepositoryImpl @Inject constructor(
    private val tokenApi: TokenApi,
) : TokensRepository {

    override suspend fun refresh(tokensModel: TokensModel): Result<TokensModel> {
        val response = tokenApi.refresh(tokensModel.toTokensDto()).awaitResponse()

        return when (response.code()) {
            200 -> Result.Success(response.body()?.toTokensModel() ?: TokensModel.EMPTY)
            400 -> Result.Failure(Error.Forbidden)
            else -> Result.Failure(Error.Unknown)
        }
    }
}
