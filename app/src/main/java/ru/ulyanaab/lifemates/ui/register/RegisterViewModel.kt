package ru.ulyanaab.lifemates.ui.register

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.domain.auth.interactor.AuthInteractor
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthEvent
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthStateHolder
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val registerMapper: RegisterMapper,
    private val authStateHolder: AuthStateHolder
) {

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var login = ""
    private var password = ""

    var handledAuthEvent: AuthEvent? = null
        private set

    var savedRegisterModel: RegisterUiModel? = null
        private set

    fun saveLoginAndPassword(login: String, password: String) {
        this.login = login
        this.password = password
    }

    fun getLoginAndPassword(): Pair<String, String> {
        return Pair(login, password)
    }

    fun onRegisterClick(
        name: String,
        age: String,
        gender: RoundedBlockUiModel?,
        showingGender: RoundedBlockUiModel?,
        description: String,
        telegram: String,
        vk: String,
        viber: String,
        whatsapp: String,
        instagram: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            _isLoading.value = true

            val model = RegisterUiModel(
                email = login,
                password = password,
                name = name,
                description = description,
                gender = gender,
                showingGender = showingGender,
                age = age,
                telegram = telegram,
                vk = vk,
                viber = viber,
                whatsapp = whatsapp,
                instagram = instagram
            )
            savedRegisterModel = model
            authInteractor.register(registerMapper.mapToDomainModel(model))

            _isLoading.value = false
        }
    }

    fun shouldHandleAuthEvent(authEvent: AuthEvent): Boolean {
        return authEvent != handledAuthEvent
    }

    fun saveHandledAuthEvent(authEvent: AuthEvent) {
        handledAuthEvent = authEvent
    }
}
