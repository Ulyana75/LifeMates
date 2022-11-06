package ru.ulyanaab.lifemates.ui.feed

import android.annotation.SuppressLint
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

    private var likeDislikeJob: Job? = null

    fun attach() {
        if (_currentUserStateFlow.value == null) {
            requestNextSingleUser()
        }
    }

    fun detach() {
        _currentUserStateFlow.value = null
        _usersAreFinishedFlow.value = false
        _matchStateFlow.value = null
        usersList.clear()
        locationWasSent = false
        locationUpdateJob?.cancel()
        locationUpdateJob = null
    }

    fun onLikeClick() {
        val model = _currentUserStateFlow.value

        likeDislikeJob = model?.let {
            CoroutineScope(Dispatchers.IO).launch {
                val isMatch = usersInteractor.like(it.id)
                if (isMatch == true) {
                    _matchStateFlow.value = otherUserMapper.mapToMatchUiModel(it)
                }
            }
        }

        requestNextSingleUser()
    }

    fun onDislikeClick() {
        val model = _currentUserStateFlow.value

        likeDislikeJob = model?.let {
            CoroutineScope(Dispatchers.IO).launch {
                usersInteractor.dislike(it.id)
            }
        }

        requestNextSingleUser()
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
                userInfoInteractor.updateLocation(null)
            }
        }
        locationWasSent = true
    }

    private fun requestNextSingleUser() {
        val nextUser = usersList.firstOrNull()
        usersList.removeFirstOrNull()

        if (nextUser != null) {
            if (usersList.size + 1 == USERS_TILL_END_TO_REQUEST) {
                // + 1 because current not rated yet
                requestNextUsersAsync(usersList.size + 1)
            }

            _currentUserStateFlow.value = otherUserMapper.mapToUiModel(nextUser)
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                _isLoading.value = true

                requestNextUsersAsync(
                    offset = 0,
                    onSuccess = {
                        requestNextSingleUser()
                    },
                    onFailure = {
                        _usersAreFinishedFlow.value = true
                    }
                ).await()

                _isLoading.value = false
            }
        }
    }

    private fun requestNextUsersAsync(
        offset: Int = 0,
        onSuccess: () -> Unit = {},
        onFailure: () -> Unit = {}
    ): Deferred<Unit> {
        return CoroutineScope(Dispatchers.IO).async {
            locationUpdateJob?.join()
            likeDislikeJob?.join()

            val nextUsersList = usersInteractor.getFeed(
                USERS_REQUEST_COUNT, offset
            )?.users

            if (nextUsersList.isNullOrEmpty()) {
                onFailure.invoke()
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
