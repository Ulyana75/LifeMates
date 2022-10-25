package ru.ulyanaab.lifemates.domain.auth.interactor

import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.auth.model.LoginModel
import ru.ulyanaab.lifemates.domain.auth.model.RegisterModel
import ru.ulyanaab.lifemates.domain.auth.repository.AuthRepository
import ru.ulyanaab.lifemates.domain.common.model.TokensModel
import ru.ulyanaab.lifemates.domain.common.repository.TokensStorage
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthEvent
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthEventType
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthStateHolder
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokensStorage: TokensStorage,
    private val authStateHolder: AuthStateHolder,
) {

    suspend fun login(loginModel: LoginModel): Boolean {
        // TODO hash password
        return proceedResult(authRepository.login(loginModel)) {
            authStateHolder.update(
                when (it.error) {
                    Error.Forbidden -> AuthEvent(AuthEventType.WRONG_PASSWORD)
                    else -> AuthEvent(AuthEventType.UNKNOWN_ERROR)
                }
            )
        }
    }

    suspend fun register(registerModel: RegisterModel): Boolean {
        // TODO hash password
        return proceedResult(authRepository.register(registerModel)) {
            authStateHolder.update(
                when (it.error) {
                    Error.Forbidden -> AuthEvent(AuthEventType.REGISTRATION_FAILED)
                    else -> AuthEvent(AuthEventType.UNKNOWN_ERROR)
                }
            )
        }
    }

    private suspend fun proceedResult(
        result: Result<TokensModel>,
        onFailure: (Result.Failure<*>) -> Unit
    ): Boolean {
        return when (result) {
            is Result.Success -> {
                tokensStorage.put(result.data)
                authStateHolder.update(AuthEvent(AuthEventType.AUTHORIZATION_SUCCESS))
                true
            }
            is Result.Failure -> {
                onFailure.invoke(result)
                false
            }
        }
    }
}
