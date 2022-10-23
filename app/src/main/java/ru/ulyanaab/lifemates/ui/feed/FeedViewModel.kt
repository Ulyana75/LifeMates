package ru.ulyanaab.lifemates.ui.feed

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.domain.users.interactor.UsersInteractor
import ru.ulyanaab.lifemates.domain.users.model.OtherUserModel
import javax.inject.Inject

class FeedViewModel @Inject constructor(
    private val usersInteractor: UsersInteractor,
) {

    private val _currentUserStateFlow: MutableStateFlow<OtherUserModel?> = MutableStateFlow(null)
    val currentUserStateFlow: StateFlow<OtherUserModel?> = _currentUserStateFlow.asStateFlow()

    private val _usersAreFinishedFlow = MutableStateFlow(false)
    val usersAreFinishedFlow: StateFlow<Boolean> = _usersAreFinishedFlow.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val usersList = mutableListOf<OtherUserModel>()
    private var currentIndex = 0

    fun requestNextSingleUser() {
        val nextUser = usersList.getOrNull(currentIndex)

        if (nextUser != null) {
            _currentUserStateFlow.value = nextUser

            if (usersList.size - (currentIndex + 1) == USERS_TILL_END_TO_REQUEST) {
                requestNextUsers()
            }

            currentIndex++
        } else {
            requestNextUsers {
                requestNextSingleUser()
            }
        }
    }

    private fun requestNextUsers(onSuccess: () -> Unit = {}) {
        CoroutineScope(Dispatchers.IO).launch {
            _isLoading.value = true

            val nextUsersList = usersInteractor.getFeed(USERS_REQUEST_COUNT)?.users

            _isLoading.value = false

            if (nextUsersList == null) return@launch

            if (nextUsersList.isEmpty()) {
                _usersAreFinishedFlow.value = true
            } else {
                usersList.addAll(nextUsersList)
                onSuccess.invoke()
            }
        }
    }

    companion object {
        private const val USERS_REQUEST_COUNT = 10
        private const val USERS_TILL_END_TO_REQUEST = 4
    }
}
