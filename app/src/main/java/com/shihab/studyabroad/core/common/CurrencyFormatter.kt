package com.shihab.studyabroad.core.common

/**
 * Utility for formatting currency display values.
 */
object CurrencyFormatter {
    fun formatBdt(amount: Long): String = "BDT $amount"

    fun formatUsd(amount: Long): String = "$$amount"
}
