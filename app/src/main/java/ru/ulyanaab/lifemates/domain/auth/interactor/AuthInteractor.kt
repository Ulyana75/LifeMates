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
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthEventsStateHolder
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthState
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthStateHolder
import ru.ulyanaab.lifemates.domain.common.state_holders.RegisterEvent
import ru.ulyanaab.lifemates.domain.common.state_holders.RegisterEventType
import ru.ulyanaab.lifemates.domain.common.state_holders.RegisterEventsStateHolder
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokensStorage: TokensStorage,
    private val authEventsStateHolder: AuthEventsStateHolder,
    private val registerEventsStateHolder: RegisterEventsStateHolder,
    private val authStateHolder: AuthStateHolder,
) {

    suspend fun updateAndGetAuthState(): AuthState {
        val tokensModel = tokensStorage.get()
        return if (tokensModel == TokensModel.EMPTY) {
            authStateHolder.update(AuthState.UNAUTHORIZED)
            AuthState.UNAUTHORIZED
        } else {
            authStateHolder.update(AuthState.AUTHORIZED)
            AuthState.AUTHORIZED
        }
    }

    suspend fun login(loginModel: LoginModel): Boolean {
        // TODO hash password
        return proceedResult(authRepository.login(loginModel)) {
            authEventsStateHolder.emit(
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
            registerEventsStateHolder.emit(
                when (it.error) {
                    Error.Forbidden -> RegisterEvent(RegisterEventType.REGISTRATION_FAILED)
                    else -> RegisterEvent(RegisterEventType.UNKNOWN_ERROR)
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
                authStateHolder.update(AuthState.AUTHORIZED)
                true
            }
            is Result.Failure -> {
                onFailure.invoke(result)
                false
            }
        }
    }
}
