package com.shihab.studyabroad.core.common

import kotlinx.datetime.LocalDate

/**
 * Formatter utilities for kotlinx.datetime LocalDate formatting.
 */
object DateFormatters {
    fun formatDisplayDate(date: LocalDate): String =
        "${date.dayOfMonth} ${date.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${date.year}"
}
