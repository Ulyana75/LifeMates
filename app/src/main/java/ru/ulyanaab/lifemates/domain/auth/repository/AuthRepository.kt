package ru.ulyanaab.lifemates.domain.auth.repository

import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.auth.model.LoginModel
import ru.ulyanaab.lifemates.domain.auth.model.RegisterModel
import ru.ulyanaab.lifemates.domain.common.model.TokensModel

interface AuthRepository {
    suspend fun login(loginModel: LoginModel): Result<TokensModel>
    suspend fun register(registerModel: RegisterModel): Result<TokensModel>
}
