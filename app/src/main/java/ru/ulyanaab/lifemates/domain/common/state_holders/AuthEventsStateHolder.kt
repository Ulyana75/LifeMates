package ru.ulyanaab.lifemates.domain.common.state_holders

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.di.AppScope
import java.util.UUID
import javax.inject.Inject

@AppScope
class AuthEventsStateHolder @Inject constructor() {
    private val _authEventsFlow = MutableSharedFlow<AuthEvent>()
    val authEventsFlow: SharedFlow<AuthEvent> = _authEventsFlow.asSharedFlow()

    fun emit(newEvent: AuthEvent) {
        CoroutineScope(Dispatchers.IO).launch {
            _authEventsFlow.emit(newEvent)
        }
    }
}

data class AuthEvent(
    val type: AuthEventType,
    val id: String = UUID.randomUUID().toString(),
)

enum class AuthEventType {
    WRONG_PASSWORD, UNKNOWN_ERROR
}
