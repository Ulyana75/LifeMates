package ru.ulyanaab.lifemates.domain.user_info.repository

import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.common.model.LocationModel
import ru.ulyanaab.lifemates.domain.user_info.model.UserInfoGetModel
import ru.ulyanaab.lifemates.domain.user_info.model.UserInfoUpdateModel

interface UserInfoRepository {
    suspend fun getUserInfo(): Result<UserInfoGetModel?>
    suspend fun updateUserInfo(newInfo: UserInfoUpdateModel): Result<Unit>
    suspend fun updateUserLocation(location: LocationModel?): Result<Unit>
}
