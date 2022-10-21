package ru.ulyanaab.lifemates.domain.user_info.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.common.interactor.RefreshTokenInteractor
import ru.ulyanaab.lifemates.domain.user_info.model.UserInfoUpdateModel
import ru.ulyanaab.lifemates.domain.user_info.repository.UserInfoRepository
import ru.ulyanaab.lifemates.domain.user_info.state_holder.UserInfoStateHolder
import javax.inject.Inject

class UserInfoInteractor @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val refreshTokenInteractor: RefreshTokenInteractor,
    private val userInfoStateHolder: UserInfoStateHolder,
) {

    fun requestUserInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            when (val result = userInfoRepository.getUserInfo()) {
                is Result.Success -> result.data?.let(userInfoStateHolder::update)
                is Result.Failure -> if (result.error is Error.Unauthorized) {
                    refreshTokenInteractor.getAndSaveNewTokens {
                        requestUserInfo()
                    }
                }
            }
        }
    }

    fun updateUserInfo(newInfo: UserInfoUpdateModel) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = userInfoRepository.updateUserInfo(newInfo)
            if (result is Result.Failure && result.error is Error.Unauthorized) {
                refreshTokenInteractor.getAndSaveNewTokens {
                    updateUserInfo(newInfo)
                }
            }
        }
    }
}
