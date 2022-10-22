package ru.ulyanaab.lifemates.data.repositoryimpl

import retrofit2.awaitResponse
import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.data.api.UserApi
import ru.ulyanaab.lifemates.data.mapper.UsersMapper
import ru.ulyanaab.lifemates.domain.users.model.FeedModel
import ru.ulyanaab.lifemates.domain.users.model.OtherUserModel
import ru.ulyanaab.lifemates.domain.users.repository.UsersRepository
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val usersMapper: UsersMapper,
) : UsersRepository {

    override suspend fun getUsers(count: Int): Result<FeedModel?> {
        val response = userApi.getUsers(count).awaitResponse()
        return when (response.code()) {
            200 -> Result.Success(response.body()?.let(usersMapper::mapToFeedModel))
            401 -> Result.Failure(Error.Unauthorized)
            else -> Result.Failure(Error.Unknown)
        }
    }

    override suspend fun getSingleUser(id: Long): Result<OtherUserModel?> {
        val response = userApi.getSingleUser(id).awaitResponse()
        return when (response.code()) {
            200 -> Result.Success(response.body()?.let(usersMapper::mapToOtherUserModel))
            401 -> Result.Failure(Error.Unauthorized)
            else -> Result.Failure(Error.Unknown)
        }
    }

    override suspend fun likeUser(id: Long): Result<Unit> {
        val response = userApi.like(id).awaitResponse()
        return when (response.code()) {
            200 -> Result.Success(Unit)
            401 -> Result.Failure(Error.Unauthorized)
            else -> Result.Failure(Error.Unknown)
        }
    }

    override suspend fun dislikeUser(id: Long): Result<Unit> {
        val response = userApi.dislike(id).awaitResponse()
        return when (response.code()) {
            200 -> Result.Success(Unit)
            401 -> Result.Failure(Error.Unauthorized)
            else -> Result.Failure(Error.Unknown)
        }
    }
}
