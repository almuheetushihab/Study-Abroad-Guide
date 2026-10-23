package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.domain.model.TestType
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidateProfileFieldsUseCaseTest {

    private lateinit var useCase: ValidateProfileFieldsUseCase

    @Before
    fun setUp() {
        useCase = ValidateProfileFieldsUseCase()
    }

    @Test
    fun `validate with all valid inputs returns valid result`() {
        val result = useCase.validate(
            level = "Master",
            subject = "Computer Science",
            cgpaInput = "3.50",
            cgpaScale = 4.0,
            testType = TestType.IELTS,
            testScoreInput = "7.0",
            experienceYearsInput = "2",
            ageInput = "25",
            budgetRange = "10-20 Lakh BDT",
            selectedCountries = listOf("de", "hu"),
        )

        assertTrue(result.isValid)
        assertNull(result.levelError)
        assertNull(result.subjectError)
        assertNull(result.cgpaError)
        assertNull(result.testScoreError)
        assertNull(result.ageError)
        assertNull(result.experienceError)
        assertNull(result.budgetError)
        assertNull(result.countriesError)
    }

    @Test
    fun `validate with boundary CGPA values on 4 scale`() {
        // CGPA = 0.0 is valid
        var result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "0.0", cgpaScale = 4.0,
            testType = TestType.NONE, testScoreInput = "", experienceYearsInput = "0",
            ageInput = "20", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertTrue(result.isCgpaValid)

        // CGPA = 4.0 is valid
        result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "4.0", cgpaScale = 4.0,
            testType = TestType.NONE, testScoreInput = "", experienceYearsInput = "0",
            ageInput = "20", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertTrue(result.isCgpaValid)

        // CGPA = 4.01 is invalid
        result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "4.01", cgpaScale = 4.0,
            testType = TestType.NONE, testScoreInput = "", experienceYearsInput = "0",
            ageInput = "20", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertFalse(result.isCgpaValid)
        assertNotNull(result.cgpaError)

        // Negative CGPA is invalid
        result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "-0.5", cgpaScale = 4.0,
            testType = TestType.NONE, testScoreInput = "", experienceYearsInput = "0",
            ageInput = "20", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertFalse(result.isCgpaValid)
    }

    @Test
    fun `validate with boundary CGPA values on 5 scale`() {
        // CGPA = 5.0 is valid on 5.0 scale
        var result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "5.0", cgpaScale = 5.0,
            testType = TestType.NONE, testScoreInput = "", experienceYearsInput = "0",
            ageInput = "20", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertTrue(result.isCgpaValid)

        // CGPA = 5.01 is invalid on 5.0 scale
        result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "5.01", cgpaScale = 5.0,
            testType = TestType.NONE, testScoreInput = "", experienceYearsInput = "0",
            ageInput = "20", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertFalse(result.isCgpaValid)
    }

    @Test
    fun `validate with IELTS score steps`() {
        // IELTS = 6.5 (half step) is valid
        var result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "3.5", cgpaScale = 4.0,
            testType = TestType.IELTS, testScoreInput = "6.5", experienceYearsInput = "0",
            ageInput = "20", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertTrue(result.isTestScoreValid)

        // IELTS = 9.0 is valid
        result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "3.5", cgpaScale = 4.0,
            testType = TestType.IELTS, testScoreInput = "9.0", experienceYearsInput = "0",
            ageInput = "20", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertTrue(result.isTestScoreValid)

        // IELTS = 6.2 (not 0.5 step) is invalid
        result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "3.5", cgpaScale = 4.0,
            testType = TestType.IELTS, testScoreInput = "6.2", experienceYearsInput = "0",
            ageInput = "20", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertFalse(result.isTestScoreValid)
        assertNotNull(result.testScoreError)

        // IELTS = 9.5 (out of 0-9 range) is invalid
        result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "3.5", cgpaScale = 4.0,
            testType = TestType.IELTS, testScoreInput = "9.5", experienceYearsInput = "0",
            ageInput = "20", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertFalse(result.isTestScoreValid)
    }

    @Test
    fun `validate with Age boundaries`() {
        // Age = 15 is valid
        var result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "3.5", cgpaScale = 4.0,
            testType = TestType.NONE, testScoreInput = "", experienceYearsInput = "0",
            ageInput = "15", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertTrue(result.isAgeValid)

        // Age = 60 is valid
        result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "3.5", cgpaScale = 4.0,
            testType = TestType.NONE, testScoreInput = "", experienceYearsInput = "0",
            ageInput = "60", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertTrue(result.isAgeValid)

        // Age = 14 is invalid
        result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "3.5", cgpaScale = 4.0,
            testType = TestType.NONE, testScoreInput = "", experienceYearsInput = "0",
            ageInput = "14", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertFalse(result.isAgeValid)

        // Age = 61 is invalid
        result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "3.5", cgpaScale = 4.0,
            testType = TestType.NONE, testScoreInput = "", experienceYearsInput = "0",
            ageInput = "61", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertFalse(result.isAgeValid)
    }

    @Test
    fun `validate with Experience boundaries`() {
        // Experience = 0 is valid
        var result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "3.5", cgpaScale = 4.0,
            testType = TestType.NONE, testScoreInput = "", experienceYearsInput = "0",
            ageInput = "25", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertTrue(result.isExperienceValid)

        // Experience = 40 is valid
        result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "3.5", cgpaScale = 4.0,
            testType = TestType.NONE, testScoreInput = "", experienceYearsInput = "40",
            ageInput = "25", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertTrue(result.isExperienceValid)

        // Experience = -1 is invalid
        result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "3.5", cgpaScale = 4.0,
            testType = TestType.NONE, testScoreInput = "", experienceYearsInput = "-1",
            ageInput = "25", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertFalse(result.isExperienceValid)

        // Experience = 41 is invalid
        result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "3.5", cgpaScale = 4.0,
            testType = TestType.NONE, testScoreInput = "", experienceYearsInput = "41",
            ageInput = "25", budgetRange = "Low", selectedCountries = listOf("de"),
        )
        assertFalse(result.isExperienceValid)
    }

    @Test
    fun `validate with blank budget or empty countries returns invalid`() {
        var result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "3.5", cgpaScale = 4.0,
            testType = TestType.NONE, testScoreInput = "", experienceYearsInput = "2",
            ageInput = "25", budgetRange = "", selectedCountries = listOf("de"),
        )
        assertFalse(result.isBudgetValid)

        result = useCase.validate(
            level = "Bachelor", subject = "CS", cgpaInput = "3.5", cgpaScale = 4.0,
            testType = TestType.NONE, testScoreInput = "", experienceYearsInput = "2",
            ageInput = "25", budgetRange = "10 Lakh", selectedCountries = emptyList(),
        )
        assertFalse(result.isCountriesValid)
    }
}
