package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.core.common.AnalyticsTracker
import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.domain.model.Report
import com.shihab.studyabroad.domain.model.ReportRefType
import com.shihab.studyabroad.domain.repository.ReportRepository
import kotlinx.datetime.Clock
import javax.inject.Inject

class SubmitReportUseCase @Inject constructor(
    private val reportRepository: ReportRepository,
    private val analyticsTracker: AnalyticsTracker,
    private val clock: Clock,
) {
    suspend operator fun invoke(refType: ReportRefType, refId: String, message: String): AppResult<Unit> {
        val trimmedMessage = message.trim()
        if (trimmedMessage.isBlank()) {
            return AppResult.Error(IllegalArgumentException("Report message cannot be blank."))
        }
        val report = Report(
            refType = refType,
            refId = refId,
            message = trimmedMessage,
            createdAt = clock.now().toEpochMilliseconds(),
        )
        val result = reportRepository.submitReport(report)
        if (result is AppResult.Success) {
            analyticsTracker.logEvent("report_submitted", mapOf("refType" to refType.name))
        }
        return result
    }
}
