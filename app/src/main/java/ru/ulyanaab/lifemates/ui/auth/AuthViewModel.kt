package ru.ulyanaab.lifemates.ui.auth

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.domain.auth.interactor.AuthInteractor
import ru.ulyanaab.lifemates.domain.auth.model.LoginModel
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthEvent
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthStateHolder
import ru.ulyanaab.lifemates.ui.common.utils.HandledAuthEventRepository
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val handledAuthEventRepository: HandledAuthEventRepository,
) {

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    var savedLoginModel: LoginModel? = null
        private set

    fun onLoginClick(login: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _isLoading.value = true
            val loginModel = LoginModel(login, password)
            savedLoginModel = loginModel
            authInteractor.login(loginModel)
            _isLoading.value = false
        }
    }

    fun shouldHandleAuthEvent(authEvent: AuthEvent): Boolean {
        return handledAuthEventRepository.getEvent() != authEvent
    }

    fun saveHandledAuthEvent(authEvent: AuthEvent) {
        handledAuthEventRepository.saveEvent(authEvent)
    }
}
