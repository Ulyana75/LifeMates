package ru.ulyanaab.lifemates.domain.interactors

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.common.Response
import ru.ulyanaab.lifemates.domain.model.LoginModel
import ru.ulyanaab.lifemates.domain.repository.AuthRepository
import ru.ulyanaab.lifemates.domain.repository.TokensRepository
import ru.ulyanaab.lifemates.ui.model.AuthEvent
import ru.ulyanaab.lifemates.ui.model.AuthorizationState
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokensRepository: TokensRepository,
    private val authorizationState: AuthorizationState,
) {

    fun login(loginModel: LoginModel) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = authRepository.login(loginModel)) {
                is Response.Success -> {
                    tokensRepository.put(response.data)
                    authorizationState.authStateFlow.value = AuthEvent.AUTHORIZATION_SUCCESS
                }
                is Response.Error -> {
                    authorizationState.authStateFlow.value = when (response.code) {
                        400 -> AuthEvent.WRONG_PASSWORD
                        else -> AuthEvent.UNKNOWN_ERROR
                    }
                }
            }
        }
    }
}
