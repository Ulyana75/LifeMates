package ru.ulyanaab.lifemates.domain.auth.interactor

import ru.ulyanaab.lifemates.BuildConfig
import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.auth.model.LoginModel
import ru.ulyanaab.lifemates.domain.auth.model.RegisterModel
import ru.ulyanaab.lifemates.domain.auth.repository.AuthRepository
import ru.ulyanaab.lifemates.domain.common.interactor.TokenInteractor
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
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokensStorage: TokensStorage,
    private val authEventsStateHolder: AuthEventsStateHolder,
    private val registerEventsStateHolder: RegisterEventsStateHolder,
    private val authStateHolder: AuthStateHolder,
    private val tokenInteractor: TokenInteractor,
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
        var modelWithHashedPassword = loginModel
        if (!BuildConfig.DEBUG) {
            modelWithHashedPassword =
                loginModel.copy(password = hashPassword(loginModel.password))
        }

        return proceedResult(authRepository.login(modelWithHashedPassword)) {
            authEventsStateHolder.emit(
                when (it.error) {
                    Error.Forbidden -> AuthEvent(AuthEventType.WRONG_PASSWORD)
                    else -> AuthEvent(AuthEventType.UNKNOWN_ERROR)
                }
            )
        }
    }

    suspend fun logOut() {
        tokenInteractor.revokeTokens()
        authStateHolder.update(AuthState.UNAUTHORIZED)
    }

    suspend fun register(registerModel: RegisterModel): Boolean {
        var modelWithHashedPassword = registerModel
        if (!BuildConfig.DEBUG) {
            modelWithHashedPassword =
                registerModel.copy(password = hashPassword(registerModel.password))
        }

        return proceedResult(authRepository.register(modelWithHashedPassword)) {
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

    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("MD5")
        val hashInBytes = md.digest(password.toByteArray(StandardCharsets.UTF_8))
        val sb = StringBuilder()
        for (b in hashInBytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }
}
