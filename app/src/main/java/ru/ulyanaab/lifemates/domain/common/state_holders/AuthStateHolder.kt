package ru.ulyanaab.lifemates.domain.common.state_holders

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.ulyanaab.lifemates.di.AppScope
import javax.inject.Inject

@AppScope
class AuthStateHolder @Inject constructor() {
    private val _authStateFlow = MutableStateFlow(AuthState.UNAUTHORIZED)
    val authStateFlow: StateFlow<AuthState> = _authStateFlow.asStateFlow()

    fun update(newState: AuthState) {
        _authStateFlow.value = newState
    }
}

enum class AuthState {
    AUTHORIZED, UNAUTHORIZED
}
