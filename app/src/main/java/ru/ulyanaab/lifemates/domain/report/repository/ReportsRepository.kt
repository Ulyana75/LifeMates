package ru.ulyanaab.lifemates.domain.report.repository

import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.report.model.ReportType

interface ReportsRepository {
    suspend fun report(userId: Long, reportType: ReportType): Result<Unit>
}
