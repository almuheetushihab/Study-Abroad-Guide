package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.domain.model.Scholarship
import com.shihab.studyabroad.domain.model.ScholarshipStatus
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Computes dynamic scholarship status (OPEN, OPENING_SOON, CLOSED).
 * Status is NEVER stored in database or domain model.
 */
class GetScholarshipStatusUseCase(
    private val clock: Clock = Clock.System,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
) {
    operator fun invoke(scholarship: Scholarship, overrideToday: LocalDate? = null): ScholarshipStatus {
        val today = overrideToday ?: clock.now().toLocalDateTime(timeZone).date
        return computeStatus(
            openDate = scholarship.openDate,
            deadline = scholarship.deadline,
            today = today,
        )
    }

    fun computeStatus(openDate: LocalDate, deadline: LocalDate, today: LocalDate): ScholarshipStatus = when {
        today > deadline -> ScholarshipStatus.CLOSED
        today >= openDate -> ScholarshipStatus.OPEN
        else -> ScholarshipStatus.OPENING_SOON
    }
}
