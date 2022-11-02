package ru.ulyanaab.lifemates.ui.feed

import android.annotation.SuppressLint
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.domain.common.model.LocationModel
import ru.ulyanaab.lifemates.domain.user_info.interactor.UserInfoInteractor
import ru.ulyanaab.lifemates.domain.users.interactor.UsersInteractor
import ru.ulyanaab.lifemates.domain.users.model.OtherUserModel
import ru.ulyanaab.lifemates.ui.common.model.OtherUserUiModel
import javax.inject.Inject

class FeedViewModel @Inject constructor(
    private val usersInteractor: UsersInteractor,
    private val otherUserMapper: OtherUserMapper,
    private val userInfoInteractor: UserInfoInteractor,
) {

    private val _currentUserStateFlow: MutableStateFlow<OtherUserUiModel?> = MutableStateFlow(null)
    val currentUserStateFlow: StateFlow<OtherUserUiModel?> = _currentUserStateFlow.asStateFlow()

    private val _currentUserModelStateFlow: MutableStateFlow<OtherUserModel?> =
        MutableStateFlow(null)

    private val _usersAreFinishedFlow = MutableStateFlow(false)
    val usersAreFinishedFlow: StateFlow<Boolean> = _usersAreFinishedFlow.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _matchStateFlow: MutableStateFlow<MatchUiModel?> = MutableStateFlow(null)
    val matchStateFlow: StateFlow<MatchUiModel?> = _matchStateFlow.asStateFlow()

    private val usersList = mutableListOf<OtherUserModel>()

    private var fusedLocationClient: FusedLocationProviderClient? = null

    private var locationWasSent = false

    private var locationUpdateJob: Job? = null

    fun attach() {
        if (_currentUserModelStateFlow.value == null) {
            requestNextSingleUser()
        }
    }

    fun onLikeClick() {
        val model = _currentUserModelStateFlow.value

        requestNextSingleUser()

        model?.let {
            CoroutineScope(Dispatchers.IO).launch {
                val isMatch = usersInteractor.like(it.id)
                if (isMatch == true) {
                    _matchStateFlow.value = otherUserMapper.mapToMatchUiModel(it)
                }
            }
        }
    }

    fun onDislikeClick() {
        val model = _currentUserModelStateFlow.value

        requestNextSingleUser()

        model?.let {
            usersInteractor.dislike(it.id)
        }
    }

    fun setFusedLocationClient(fusedLocationProviderClient: FusedLocationProviderClient) {
        this.fusedLocationClient = fusedLocationProviderClient
    }

    @SuppressLint("MissingPermission")
    fun onLocationPermissionGranted(afterRequestCallback: () -> Unit = {}) {
        if (!locationWasSent) {
            fusedLocationClient?.lastLocation?.addOnSuccessListener {
                locationUpdateJob = CoroutineScope(Dispatchers.IO).launch {
                    userInfoInteractor.updateLocation(
                        LocationModel(it.latitude, it.longitude)
                    )
                }
                afterRequestCallback.invoke()
            }
            locationWasSent = true
        } else {
            afterRequestCallback.invoke()
        }
    }

    fun onLocationPermissionNotGranted() {
        // TODO send null
        if (!locationWasSent) {
            locationUpdateJob = CoroutineScope(Dispatchers.IO).launch {
                userInfoInteractor.updateLocation(
                    LocationModel(0.0, 0.0)
                )
            }
        }
        locationWasSent = true
    }

    private fun requestNextSingleUser() {
        val nextUser = usersList.firstOrNull()
        usersList.removeFirstOrNull()

        if (nextUser != null) {
            _currentUserModelStateFlow.value = nextUser

            if (usersList.size == USERS_TILL_END_TO_REQUEST) {
                requestNextUsersAsync()
            }

            _currentUserStateFlow.value = otherUserMapper.mapToUiModel(nextUser)
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                _isLoading.value = true

                requestNextUsersAsync {
                    requestNextSingleUser()
                }.await()

                _isLoading.value = false
            }
        }
    }

    private fun requestNextUsersAsync(onSuccess: () -> Unit = {}): Deferred<Unit> {
        return CoroutineScope(Dispatchers.IO).async {
            locationUpdateJob?.join()

            val nextUsersList = usersInteractor.getFeed(USERS_REQUEST_COUNT)?.users ?: return@async

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
