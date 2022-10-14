package ru.ulyanaab.lifemates.data.repositoryimpl

import retrofit2.awaitResponse
import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.data.api.UserApi
import ru.ulyanaab.lifemates.data.dto.common.TokensDto
import ru.ulyanaab.lifemates.data.dto.request.LoginRequestDto
import ru.ulyanaab.lifemates.domain.model.LoginModel
import ru.ulyanaab.lifemates.domain.model.TokensModel
import ru.ulyanaab.lifemates.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
) : AuthRepository {

    override suspend fun login(loginModel: LoginModel): Result<TokensModel> {
        val response = userApi.login(loginModel.toLoginRequestDto()).awaitResponse()
        return when (response.code()) {
            200 -> Result.Success(response.body()?.toTokensModel() ?: TokensModel.EMPTY)
            400 -> Result.Failure(Error.Forbidden)
            else -> Result.Failure(Error.Unknown)
        }
    }
}

private fun LoginModel.toLoginRequestDto(): LoginRequestDto {
    return LoginRequestDto(email, password)
}

private fun TokensDto.toTokensModel(): TokensModel {
    return TokensModel(accessToken, refreshToken)
}
