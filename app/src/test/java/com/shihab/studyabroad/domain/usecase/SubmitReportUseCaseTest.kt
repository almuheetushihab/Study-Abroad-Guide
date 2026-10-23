package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.domain.model.ReportRefType
import com.shihab.studyabroad.domain.repository.ReportRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SubmitReportUseCaseTest {

    private lateinit var reportRepository: ReportRepository
    private lateinit var submitReportUseCase: SubmitReportUseCase

    @Before
    fun setUp() {
        reportRepository = mockk(relaxed = true)
        submitReportUseCase = SubmitReportUseCase(reportRepository)
    }

    @Test
    fun `invoke with valid message submits report to repository successfully`() = runTest {
        coEvery { reportRepository.submitReport(any()) } returns AppResult.Success(Unit)

        val result = submitReportUseCase(
            refType = ReportRefType.SCHOLARSHIP,
            refId = "sch_stipendium_hungaricum",
            message = "Deadline is incorrect on the page",
        )

        assertTrue(result is AppResult.Success)
        coVerify(exactly = 1) {
            reportRepository.submitReport(
                match {
                    it.refType == ReportRefType.SCHOLARSHIP &&
                        it.refId == "sch_stipendium_hungaricum" &&
                        it.message == "Deadline is incorrect on the page"
                },
            )
        }
    }

    @Test
    fun `invoke with blank message returns error and does not submit to repository`() = runTest {
        val result = submitReportUseCase(
            refType = ReportRefType.COUNTRY,
            refId = "HU",
            message = "   ",
        )

        assertTrue(result is AppResult.Error)
        coVerify(exactly = 0) { reportRepository.submitReport(any()) }
    }
}
