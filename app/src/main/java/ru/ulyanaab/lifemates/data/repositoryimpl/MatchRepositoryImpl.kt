package ru.ulyanaab.lifemates.data.repositoryimpl

import retrofit2.awaitResponse
import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.data.api.MatchApi
import ru.ulyanaab.lifemates.data.mapper.UsersMapper
import ru.ulyanaab.lifemates.domain.match.repository.MatchRepository
import ru.ulyanaab.lifemates.domain.users.model.OtherUserModel
import javax.inject.Inject

class MatchRepositoryImpl @Inject constructor(
    private val matchApi: MatchApi,
    private val usersMapper: UsersMapper,
) : MatchRepository {

    override suspend fun getMatches(offset: Int, limit: Int): Result<List<OtherUserModel>?> {
        val response = matchApi.getMatches(offset, limit).awaitResponse()
        return when (response.code()) {
            200 -> Result.Success(response.body()?.let {
                it.matches.map { matchDto ->
                    usersMapper.mapToOtherUserModel(matchDto.user)
                }
            })
            401 -> Result.Failure(Error.Unauthorized)
            else -> Result.Failure(Error.Unknown)
        }
    }
}
