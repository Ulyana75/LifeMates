package ru.ulyanaab.lifemates.domain.interactors

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.model.LoginModel
import ru.ulyanaab.lifemates.domain.repository.AuthRepository
import ru.ulyanaab.lifemates.domain.repository.TokensStorage
import ru.ulyanaab.lifemates.ui.model.AuthEvent
import ru.ulyanaab.lifemates.ui.model.AuthorizationState
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokensStorage: TokensStorage,
    private val authorizationState: AuthorizationState,
) {

    fun login(loginModel: LoginModel) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val result = authRepository.login(loginModel)) {
                is Result.Success -> {
                    tokensStorage.put(result.data)
                    authorizationState.authStateFlow.value = AuthEvent.AUTHORIZATION_SUCCESS
                }
                is Result.Failure -> {
                    authorizationState.authStateFlow.value = when (result.error) {
                        Error.Forbidden -> AuthEvent.WRONG_PASSWORD
                        else -> AuthEvent.UNKNOWN_ERROR
                    }
                }
            }
        }
    }
}
