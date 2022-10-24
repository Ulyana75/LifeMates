package ru.ulyanaab.lifemates.ui.auth

import kotlinx.coroutines.flow.StateFlow
import ru.ulyanaab.lifemates.domain.auth.interactor.AuthInteractor
import ru.ulyanaab.lifemates.domain.auth.model.LoginModel
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthEvent
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthStateHolder
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    authStateHolder: AuthStateHolder,
) {

    // TODO make loading while authorizing
    // TODO add dialog with auth events

    val authEventsFlow: StateFlow<AuthEvent> = authStateHolder.authStateFlow

    fun onLoginClick(login: String, password: String) {
        authInteractor.login(LoginModel(login, password))
    }

    fun onRegisterClick() {
    }

    fun onSubmitRegisterClick(registerUiModel: RegisterUiModel) {

    }
}
