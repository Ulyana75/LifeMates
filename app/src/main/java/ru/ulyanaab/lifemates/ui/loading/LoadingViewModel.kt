package ru.ulyanaab.lifemates.ui.loading

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.domain.auth.interactor.AuthInteractor
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthState
import javax.inject.Inject

class LoadingViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
) {
    private val _actualAuthState: MutableStateFlow<AuthState?> = MutableStateFlow(null)
    val actualAuthState: StateFlow<AuthState?> = _actualAuthState.asStateFlow()

    fun attach() {
        CoroutineScope(Dispatchers.IO).launch {
            val state = authInteractor.updateAndGetAuthState()
            _actualAuthState.value = state
        }
    }
}
