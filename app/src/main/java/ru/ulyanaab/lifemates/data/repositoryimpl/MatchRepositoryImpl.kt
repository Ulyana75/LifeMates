package ru.ulyanaab.lifemates.data.repositoryimpl

import retrofit2.awaitResponse
import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.data.api.MatchApi
import ru.ulyanaab.lifemates.data.mapper.UsersMapper
import ru.ulyanaab.lifemates.domain.match.model.MatchModel
import ru.ulyanaab.lifemates.domain.match.model.MatchesModel
import ru.ulyanaab.lifemates.domain.match.repository.MatchRepository
import javax.inject.Inject

class MatchRepositoryImpl @Inject constructor(
    private val matchApi: MatchApi,
    private val usersMapper: UsersMapper,
) : MatchRepository {

    override suspend fun getMatches(offset: Int, limit: Int): Result<MatchesModel?> {
        val response = matchApi.getMatches(offset, limit).awaitResponse()
        return when (response.code()) {
            200 -> Result.Success(response.body()?.let {
                MatchesModel(
                    matches = it.matches.map { matchDto ->
                        MatchModel(
                            user = usersMapper.mapToOtherUserModel(matchDto.user),
                            isSeen = matchDto.isSeen,
                        )
                    },
                    count = it.count,
                )
            })
            401 -> Result.Failure(Error.Unauthorized)
            else -> Result.Failure(Error.Unknown)
        }
    }
}
