package com.shihab.studyabroad.core.common

object PhoneNumberNormalizer {
    fun normalize(rawNumber: String): String {
        val cleaned = rawNumber.replace(Regex("[^0-9+]"), "")
        return when {
            cleaned.startsWith("+880") -> cleaned
            cleaned.startsWith("880") -> "+$cleaned"
            cleaned.startsWith("01") -> "+880${cleaned.substring(1)}"
            cleaned.startsWith("1") && cleaned.length == 10 -> "+880$cleaned"
            cleaned.startsWith("+") -> cleaned
            else -> "+880$cleaned"
        }
    }
}
