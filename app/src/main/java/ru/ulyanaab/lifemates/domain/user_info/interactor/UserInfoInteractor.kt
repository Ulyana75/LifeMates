package ru.ulyanaab.lifemates.domain.user_info.interactor

import ru.ulyanaab.lifemates.domain.common.model.LocationModel
import ru.ulyanaab.lifemates.domain.common.utils.ResultProcessorWithTokensRefreshing
import ru.ulyanaab.lifemates.domain.user_info.model.UserInfoGetModel
import ru.ulyanaab.lifemates.domain.user_info.model.UserInfoUpdateModel
import ru.ulyanaab.lifemates.domain.user_info.repository.UserInfoRepository
import javax.inject.Inject

class UserInfoInteractor @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val resultProcessorWithTokensRefreshing: ResultProcessorWithTokensRefreshing,
) {

    suspend fun getUserInfo(): UserInfoGetModel? {
        return resultProcessorWithTokensRefreshing.proceedAndReturn(
            resultProducer = {
                userInfoRepository.getUserInfo()
            },
            onTokensRefreshedSuccessfully = {
                getUserInfo()
            }
        )
    }

    fun updateUserInfo(newInfo: UserInfoUpdateModel) {
        resultProcessorWithTokensRefreshing.proceed(
            resultProducer = {
                userInfoRepository.updateUserInfo(newInfo)
            },
            onTokensRefreshedSuccessfully = {
                updateUserInfo(newInfo)
            }
        )
    }

   suspend fun updateLocation(location: LocationModel) {
       resultProcessorWithTokensRefreshing.proceedAndReturn(
            resultProducer = {
                userInfoRepository.updateUserLocation(location)
            },
            onTokensRefreshedSuccessfully = {
                updateLocation(location)
            }
        )
    }
}
