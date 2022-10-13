package ru.ulyanaab.lifemates.ui.model

import kotlinx.coroutines.flow.MutableStateFlow
import ru.ulyanaab.lifemates.di.AppScope
import javax.inject.Inject

@AppScope
class AuthorizationState @Inject constructor() {
    val authStateFlow = MutableStateFlow(AuthEvent.UNAUTHORIZED)
}

enum class AuthEvent {
    AUTHORIZATION_SUCCESS, WRONG_PASSWORD, UNKNOWN_ERROR, UNAUTHORIZED
}
