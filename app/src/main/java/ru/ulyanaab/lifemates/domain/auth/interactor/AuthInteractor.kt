package ru.ulyanaab.lifemates.domain.auth.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.auth.model.LoginModel
import ru.ulyanaab.lifemates.domain.auth.model.RegisterModel
import ru.ulyanaab.lifemates.domain.common.model.TokensModel
import ru.ulyanaab.lifemates.domain.auth.repository.AuthRepository
import ru.ulyanaab.lifemates.domain.common.repository.TokensStorage
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthEvent
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthStateHolder
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokensStorage: TokensStorage,
    private val authStateHolder: AuthStateHolder,
) {

    fun login(loginModel: LoginModel) {
        // TODO hash password
        CoroutineScope(Dispatchers.IO).launch {
            proceedResult(authRepository.login(loginModel))
        }
    }

    fun register(registerModel: RegisterModel) {
        // TODO hash password
        CoroutineScope(Dispatchers.IO).launch {
            proceedResult(authRepository.register(registerModel))
        }
    }

    private suspend fun proceedResult(result: Result<TokensModel>) {
        when (result) {
            is Result.Success -> {
                tokensStorage.put(result.data)
                authStateHolder.update(AuthEvent.AUTHORIZATION_SUCCESS)
            }
            is Result.Failure -> {
                authStateHolder.update(
                    when (result.error) {
                        Error.Forbidden -> AuthEvent.WRONG_PASSWORD
                        else -> AuthEvent.UNKNOWN_ERROR
                    }
                )
            }
        }
    }
}
