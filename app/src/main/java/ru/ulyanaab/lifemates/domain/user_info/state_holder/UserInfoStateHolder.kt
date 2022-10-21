package ru.ulyanaab.lifemates.domain.user_info.state_holder

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.ulyanaab.lifemates.di.AppScope
import ru.ulyanaab.lifemates.domain.user_info.model.UserInfoGetModel
import javax.inject.Inject

@AppScope
class UserInfoStateHolder @Inject constructor() {
    private val _userInfoStateFlow: MutableStateFlow<UserInfoGetModel?> = MutableStateFlow(null)
    val userInfoStateFlow: StateFlow<UserInfoGetModel?> = _userInfoStateFlow.asStateFlow()

    fun update(newState: UserInfoGetModel) {
        _userInfoStateFlow.value = newState
    }
}
