package ru.ulyanaab.lifemates.data.repositoryimpl

import retrofit2.awaitResponse
import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.data.api.InterestsApi
import ru.ulyanaab.lifemates.data.mapper.InterestsMapper
import ru.ulyanaab.lifemates.domain.common.model.InterestModel
import ru.ulyanaab.lifemates.domain.common.repository.InterestsRepository
import javax.inject.Inject

class InterestsRepositoryImpl @Inject constructor(
    private val interestsApi: InterestsApi,
    private val interestsMapper: InterestsMapper,
) : InterestsRepository {

    override suspend fun getInterests(): Result<List<InterestModel>> {
        val response = interestsApi.getInterests().awaitResponse()
        return when (response.code()) {
            200 -> Result.Success(
                response.body()?.interests?.map(interestsMapper::mapToModel) ?: emptyList()
            )
            401 -> Result.Failure(Error.Unauthorized)
            else -> Result.Failure(Error.Unknown)
        }
    }

}
