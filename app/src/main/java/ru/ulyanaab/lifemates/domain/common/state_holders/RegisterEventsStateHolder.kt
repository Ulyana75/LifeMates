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
class RegisterEventsStateHolder @Inject constructor() {
    private val _registerEventsFlow = MutableSharedFlow<RegisterEvent>()
    val registerEventsFlow: SharedFlow<RegisterEvent> = _registerEventsFlow.asSharedFlow()

    fun emit(newEvent: RegisterEvent) {
        CoroutineScope(Dispatchers.IO).launch {
            _registerEventsFlow.emit(newEvent)
        }
    }
}

data class RegisterEvent(
    val type: RegisterEventType,
    val id: String = UUID.randomUUID().toString(),
)

enum class RegisterEventType {
    UNKNOWN_ERROR, REGISTRATION_FAILED
}
