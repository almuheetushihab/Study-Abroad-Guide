package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.domain.model.Eligibility
import com.shihab.studyabroad.domain.model.FundingType
import com.shihab.studyabroad.domain.model.Scholarship
import com.shihab.studyabroad.domain.model.ScholarshipStatus
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class GetScholarshipStatusUseCaseTest {

    private val useCase = GetScholarshipStatusUseCase()

    private val sampleScholarship = Scholarship(
        id = "test-scholarship",
        name = "Test Scholarship",
        countryId = "de",
        level = listOf("MSc"),
        funding = FundingType.FULL,
        openDate = LocalDate(2025, 6, 1),
        deadline = LocalDate(2025, 8, 31),
        applyUrl = "https://example.com/apply",
        sourceUrl = "https://example.com/source",
        verifiedOn = LocalDate(2025, 1, 1),
        eligibility = Eligibility(minCgpa = 3.0),
    )

    @Test
    fun `status is OPENING_SOON when today is before open date`() {
        val today = LocalDate(2025, 5, 31)
        val status = useCase(sampleScholarship, overrideToday = today)
        assertEquals(ScholarshipStatus.OPENING_SOON, status)
    }

    @Test
    fun `status is OPEN on exact open date boundary`() {
        val today = LocalDate(2025, 6, 1)
        val status = useCase(sampleScholarship, overrideToday = today)
        assertEquals(ScholarshipStatus.OPEN, status)
    }

    @Test
    fun `status is OPEN during open period`() {
        val today = LocalDate(2025, 7, 15)
        val status = useCase(sampleScholarship, overrideToday = today)
        assertEquals(ScholarshipStatus.OPEN, status)
    }

    @Test
    fun `status is OPEN on exact deadline date boundary`() {
        val today = LocalDate(2025, 8, 31)
        val status = useCase(sampleScholarship, overrideToday = today)
        assertEquals(ScholarshipStatus.OPEN, status)
    }

    @Test
    fun `status is CLOSED when today is after deadline`() {
        val today = LocalDate(2025, 9, 1)
        val status = useCase(sampleScholarship, overrideToday = today)
        assertEquals(ScholarshipStatus.CLOSED, status)
    }
}
