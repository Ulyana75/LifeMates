package ru.ulyanaab.lifemates.domain.common.state_holders

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.ulyanaab.lifemates.di.AppScope
import java.util.UUID
import javax.inject.Inject

@AppScope
class AuthStateHolder @Inject constructor() {
    private val _authStateFlow = MutableStateFlow(AuthEvent(AuthEventType.UNAUTHORIZED))
    val authStateFlow: StateFlow<AuthEvent> = _authStateFlow.asStateFlow()

    fun update(newState: AuthEvent) {
        _authStateFlow.value = newState
    }
}

data class AuthEvent(
    val type: AuthEventType,
    val id: String = UUID.randomUUID().toString()
)

enum class AuthEventType {
    AUTHORIZATION_SUCCESS, WRONG_PASSWORD, UNKNOWN_ERROR, UNAUTHORIZED, REGISTRATION_FAILED
}
