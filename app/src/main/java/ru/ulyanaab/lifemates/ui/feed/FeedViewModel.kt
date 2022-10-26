package ru.ulyanaab.lifemates.ui.feed

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.domain.users.interactor.UsersInteractor
import ru.ulyanaab.lifemates.domain.users.model.OtherUserModel
import ru.ulyanaab.lifemates.ui.common.model.OtherUserUiModel
import javax.inject.Inject

class FeedViewModel @Inject constructor(
    private val usersInteractor: UsersInteractor,
    private val otherUserMapper: OtherUserMapper,
) {

    private val _currentUserStateFlow: MutableStateFlow<OtherUserUiModel?> = MutableStateFlow(null)
    val currentUserStateFlow: StateFlow<OtherUserUiModel?> = _currentUserStateFlow.asStateFlow()

    private val _currentUserIdStateFlow: MutableStateFlow<Long?> = MutableStateFlow(null)

    private val _usersAreFinishedFlow = MutableStateFlow(false)
    val usersAreFinishedFlow: StateFlow<Boolean> = _usersAreFinishedFlow.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val usersList = mutableListOf<OtherUserModel>()
    private var currentIndex = 0

    fun attach() {
        requestNextSingleUser()
    }

    fun onLikeClick() {
        requestNextSingleUser()
    }

    fun onDislikeClick() {
        requestNextSingleUser()
    }

    private fun requestNextSingleUser() {
        val nextUser = usersList.getOrNull(currentIndex)

        if (nextUser != null) {
            _currentUserStateFlow.value = otherUserMapper.mapToUiModel(nextUser)
            _currentUserIdStateFlow.value = nextUser.id

            if (usersList.size - (currentIndex + 1) == USERS_TILL_END_TO_REQUEST) {
                requestNextUsersAsync()
            }

            currentIndex++
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                _isLoading.value = true
                requestNextUsersAsync().await()
                _isLoading.value = false
                if (!_usersAreFinishedFlow.value) {
                    requestNextSingleUser()
                }
            }
        }
    }

    private fun requestNextUsersAsync(): Deferred<Unit> {
        return CoroutineScope(Dispatchers.IO).async {
            val nextUsersList = usersInteractor.getFeed(USERS_REQUEST_COUNT)?.users ?: return@async

            if (nextUsersList.isEmpty()) {
                _usersAreFinishedFlow.value = true
            } else {
                usersList.addAll(nextUsersList)
            }
        }
    }

    companion object {
        private const val USERS_REQUEST_COUNT = 10
        private const val USERS_TILL_END_TO_REQUEST = 4
    }
}
