package ru.ulyanaab.lifemates.data.repositoryimpl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import ru.ulyanaab.lifemates.common.Response
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

    override suspend fun login(loginModel: LoginModel): Response<TokensModel> {
        withContext(Dispatchers.IO) {
            userApi.login(loginModel.toLoginRequestDto()).execute()
        }

    }
}

private fun LoginModel.toLoginRequestDto(): LoginRequestDto {
    return LoginRequestDto(email, password)
}
