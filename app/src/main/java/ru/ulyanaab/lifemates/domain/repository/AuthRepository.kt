package ru.ulyanaab.lifemates.domain.repository

import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.model.LoginModel
import ru.ulyanaab.lifemates.domain.model.TokensModel

interface AuthRepository {
    suspend fun login(loginModel: LoginModel): Result<TokensModel>
}
