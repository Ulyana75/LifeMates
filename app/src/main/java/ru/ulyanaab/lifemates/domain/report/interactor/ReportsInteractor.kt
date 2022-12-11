package ru.ulyanaab.lifemates.domain.report.interactor

import ru.ulyanaab.lifemates.domain.common.utils.ResultProcessorWithTokensRefreshing
import ru.ulyanaab.lifemates.domain.report.model.ReportType
import ru.ulyanaab.lifemates.domain.report.repository.ReportsRepository
import javax.inject.Inject

class ReportsInteractor @Inject constructor(
    private val reportsRepository: ReportsRepository,
    private val resultProcessorWithTokensRefreshing: ResultProcessorWithTokensRefreshing,
) {

    fun report(userId: Long, reportType: ReportType) {
        resultProcessorWithTokensRefreshing.proceed(
            resultProducer = {
                reportsRepository.report(userId, reportType)
            },
            onTokensRefreshedSuccessfully = {
                report(userId, reportType)
            }
        )
    }
}
