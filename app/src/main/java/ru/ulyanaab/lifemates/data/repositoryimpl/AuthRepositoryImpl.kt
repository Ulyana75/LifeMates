package ru.ulyanaab.lifemates.data.repositoryimpl

import retrofit2.awaitResponse
import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.data.api.AuthApi
import ru.ulyanaab.lifemates.data.dto.common.toTokensModel
import ru.ulyanaab.lifemates.data.dto.request.LoginRequestDto
import ru.ulyanaab.lifemates.data.mapper.RegisterMapper
import ru.ulyanaab.lifemates.domain.auth.model.LoginModel
import ru.ulyanaab.lifemates.domain.auth.model.RegisterModel
import ru.ulyanaab.lifemates.domain.common.model.TokensModel
import ru.ulyanaab.lifemates.domain.auth.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val registerMapper: RegisterMapper,
) : AuthRepository {

    override suspend fun login(loginModel: LoginModel): Result<TokensModel> {
        val response = authApi.login(loginModel.toLoginRequestDto()).awaitResponse()
        return when (response.code()) {
            200 -> Result.Success(response.body()?.toTokensModel() ?: TokensModel.EMPTY)
            400 -> Result.Failure(Error.Forbidden)
            else -> Result.Failure(Error.Unknown)
        }
    }

    override suspend fun register(registerModel: RegisterModel): Result<TokensModel> {
        val response = authApi.register(registerMapper.map(registerModel)).awaitResponse()
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
