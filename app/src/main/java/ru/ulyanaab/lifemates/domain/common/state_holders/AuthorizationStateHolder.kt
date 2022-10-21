package ru.ulyanaab.lifemates.domain.common.state_holders

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.ulyanaab.lifemates.di.AppScope
import javax.inject.Inject

@AppScope
class AuthorizationStateHolder @Inject constructor() {
    private val _authStateFlow = MutableStateFlow(AuthEvent.UNAUTHORIZED)
    val authStateFlow: StateFlow<AuthEvent> = _authStateFlow.asStateFlow()

    fun update(newState: AuthEvent) {
        _authStateFlow.value = newState
    }
}

enum class AuthEvent {
    AUTHORIZATION_SUCCESS, WRONG_PASSWORD, UNKNOWN_ERROR, UNAUTHORIZED
}
