package com.shihab.studyabroad.domain.usecase

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IsRecordStaleUseCaseTest {

    private val fixedClock = object : Clock {
        override fun now(): Instant = Instant.fromEpochMilliseconds(1000L)
    }

    private val useCase = IsRecordStaleUseCase(fixedClock)
    private val today = LocalDate(2025, 4, 15)

    @Test
    fun `returns false when verified date is within 90 days`() {
        val verifiedOn = LocalDate(2025, 3, 1) // 45 days ago
        val isStale = useCase(verifiedOn, overrideToday = today)
        assertFalse(isStale)
    }

    @Test
    fun `returns false on 90 days boundary`() {
        val verifiedOn = LocalDate(2025, 1, 15) // exactly 90 days ago
        val isStale = useCase(verifiedOn, overrideToday = today)
        assertFalse(isStale)
    }

    @Test
    fun `returns true when verified date is older than 90 days`() {
        val verifiedOn = LocalDate(2025, 1, 10) // 95 days ago
        val isStale = useCase(verifiedOn, overrideToday = today)
        assertTrue(isStale)
    }
}
