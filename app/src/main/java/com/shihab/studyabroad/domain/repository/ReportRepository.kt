package com.shihab.studyabroad.domain.repository

import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.domain.model.Report
import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    suspend fun submitReport(report: Report): AppResult<Unit>
    fun getAllReports(): Flow<List<Report>>
    suspend fun clearAllReports()
}
