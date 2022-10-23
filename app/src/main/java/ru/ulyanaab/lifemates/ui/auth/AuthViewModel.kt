package ru.ulyanaab.lifemates.ui.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    val authEventsFlow: StateFlow<AuthEvent> = authStateHolder.authStateFlow

    private val _shouldShowRegisterFormFlow = MutableStateFlow(false)
    val shouldShowRegisterFormFlow: StateFlow<Boolean> = _shouldShowRegisterFormFlow.asStateFlow()

    private val _shouldNavigateToNextScreenFlow = MutableStateFlow(false)
    val shouldNavigateToNextScreenFlow: StateFlow<Boolean> = _shouldShowRegisterFormFlow.asStateFlow()

    fun onLoginClick(login: String, password: String) {
        authInteractor.login(LoginModel(login, password))
    }

    fun onRegisterClick() {
        _shouldShowRegisterFormFlow.value = true
    }

    fun onSubmitRegisterClick(registerUiModel: RegisterUiModel) {

    }
}
