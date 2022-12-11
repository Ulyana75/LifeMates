package ru.ulyanaab.lifemates.data.repositoryimpl

import retrofit2.awaitResponse
import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.data.api.ReportsApi
import ru.ulyanaab.lifemates.data.dto.request.ReportRequestDto
import ru.ulyanaab.lifemates.data.dto.request.ReportTypeDto
import ru.ulyanaab.lifemates.domain.report.model.ReportType
import ru.ulyanaab.lifemates.domain.report.repository.ReportsRepository
import javax.inject.Inject

class ReportsRepositoryImpl @Inject constructor(
    private val reportsApi: ReportsApi
) : ReportsRepository {

    override suspend fun report(userId: Long, reportType: ReportType): Result<Unit> {
        val response = reportsApi.report(
            ReportRequestDto(
                userId = userId,
                type = when (reportType) {
                    ReportType.AGGRESSIVENESS -> ReportTypeDto.AGGRESSIVENESS
                    ReportType.HARASSMENT -> ReportTypeDto.HARASSMENT
                    ReportType.PROFILE_CHEATING -> ReportTypeDto.PROFILE_CHEATING
                }
            )
        ).awaitResponse()

        return when (response.code()) {
            200 -> Result.Success(Unit)
            401 -> Result.Failure(Error.Unauthorized)
            else -> Result.Failure(Error.Unknown)
        }
    }
}
