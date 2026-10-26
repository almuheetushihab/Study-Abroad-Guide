package com.shihab.studyabroad.domain.usecase

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime

const val STALE_LIMIT_DAYS = 90

/**
 * Checks if a record verifiedOn date is older than 90 days (FR-TRU-002).
 */
class IsRecordStaleUseCase(private val clock: Clock, private val timeZone: TimeZone = TimeZone.currentSystemDefault()) {
    operator fun invoke(verifiedOn: LocalDate, overrideToday: LocalDate? = null): Boolean {
        val today = overrideToday ?: clock.now().toLocalDateTime(timeZone).date
        val daysBetween = verifiedOn.daysUntil(today)
        return daysBetween > STALE_LIMIT_DAYS
    }
}
