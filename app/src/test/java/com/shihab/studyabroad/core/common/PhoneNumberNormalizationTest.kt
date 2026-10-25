package com.shihab.studyabroad.core.common

import org.junit.Assert.assertEquals
import org.junit.Test

class PhoneNumberNormalizationTest {

    @Test
    fun `normalizes local 11-digit Bangladeshi number starting with 01`() {
        val result = PhoneNumberNormalizer.normalize("01712345678")
        assertEquals("+8801712345678", result)
    }

    @Test
    fun `normalizes formatted local number with dashes and spaces`() {
        val result = PhoneNumberNormalizer.normalize("018-1234 5678")
        assertEquals("+8801812345678", result)
    }

    @Test
    fun `preserves number already starting with +880`() {
        val result = PhoneNumberNormalizer.normalize("+8801712345678")
        assertEquals("+8801712345678", result)
    }

    @Test
    fun `prepends plus to number starting with 880`() {
        val result = PhoneNumberNormalizer.normalize("8801712345678")
        assertEquals("+8801712345678", result)
    }

    @Test
    fun `normalizes 10-digit number without leading 0`() {
        val result = PhoneNumberNormalizer.normalize("1712345678")
        assertEquals("+8801712345678", result)
    }
}
