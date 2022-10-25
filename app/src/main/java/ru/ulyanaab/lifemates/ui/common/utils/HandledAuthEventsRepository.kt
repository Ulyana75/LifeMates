package ru.ulyanaab.lifemates.ui.common.utils

import ru.ulyanaab.lifemates.di.AppScope
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthEvent
import javax.inject.Inject

@AppScope
class HandledAuthEventRepository @Inject constructor() {

    private var lastHandledEvent: AuthEvent? = null

    fun saveEvent(authEvent: AuthEvent) {
        lastHandledEvent = authEvent
    }

    fun getEvent(): AuthEvent? {
        return lastHandledEvent
    }
}
