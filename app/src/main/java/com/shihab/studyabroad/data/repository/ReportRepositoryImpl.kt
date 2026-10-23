package com.shihab.studyabroad.data.repository

import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.data.local.dao.ReportDao
import com.shihab.studyabroad.data.local.entity.ReportEntity
import com.shihab.studyabroad.domain.model.Report
import com.shihab.studyabroad.domain.model.ReportRefType
import com.shihab.studyabroad.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(private val reportDao: ReportDao) : ReportRepository {

    override suspend fun submitReport(report: Report): AppResult<Unit> = try {
        val id = if (report.id.isBlank()) UUID.randomUUID().toString() else report.id
        val entity = ReportEntity(
            id = id,
            refType = report.refType.name,
            refId = report.refId,
            message = report.message,
            createdAt = report.createdAt,
        )
        reportDao.insertReport(entity)
        AppResult.Success(Unit)
    } catch (e: Exception) {
        AppResult.Error(e)
    }

    override fun getAllReports(): Flow<List<Report>> = reportDao.getAllReports().map { entities ->
        entities.map { entity ->
            Report(
                id = entity.id,
                refType = try {
                    ReportRefType.valueOf(entity.refType)
                } catch (_: Exception) {
                    ReportRefType.SCHOLARSHIP
                },
                refId = entity.refId,
                message = entity.message,
                createdAt = entity.createdAt,
            )
        }
    }

    override suspend fun clearAllReports() {
        reportDao.deleteAllReports()
    }
}
