package ru.ulyanaab.lifemates.data.repositoryimpl

import retrofit2.awaitResponse
import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.data.api.MeApi
import ru.ulyanaab.lifemates.data.mapper.MeMapper
import ru.ulyanaab.lifemates.domain.user_info.model.UserInfoGetModel
import ru.ulyanaab.lifemates.domain.user_info.model.UserInfoUpdateModel
import ru.ulyanaab.lifemates.domain.user_info.repository.UserInfoRepository
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
    private val meApi: MeApi,
    private val meMapper: MeMapper,
) : UserInfoRepository {

    override suspend fun getUserInfo(): Result<UserInfoGetModel?> {
        val response = meApi.getMe().awaitResponse()
        return when (response.code()) {
            200 -> Result.Success(response.body()?.let(meMapper::mapToModel))
            401 -> Result.Failure(Error.Unauthorized)
            else -> Result.Failure(Error.Unknown)
        }
    }

    override suspend fun updateUserInfo(newInfo: UserInfoUpdateModel): Result<Unit> {
        val response = meApi.putMe(meMapper.mapToDto(newInfo)).awaitResponse()
        return when (response.code()) {
            200 -> Result.Success(Unit)
            401 -> Result.Failure(Error.Unauthorized)
            else -> Result.Failure(Error.Unknown)
        }
    }
}
