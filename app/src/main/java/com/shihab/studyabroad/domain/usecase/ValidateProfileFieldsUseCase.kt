package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.TestType
import javax.inject.Inject
import kotlin.math.abs

data class ProfileValidationResult(
    val isLevelValid: Boolean = true,
    val levelError: UiText? = null,
    val isSubjectValid: Boolean = true,
    val subjectError: UiText? = null,
    val isCgpaValid: Boolean = true,
    val cgpaError: UiText? = null,
    val isTestScoreValid: Boolean = true,
    val testScoreError: UiText? = null,
    val isAgeValid: Boolean = true,
    val ageError: UiText? = null,
    val isExperienceValid: Boolean = true,
    val experienceError: UiText? = null,
    val isBudgetValid: Boolean = true,
    val budgetError: UiText? = null,
    val isCountriesValid: Boolean = true,
    val countriesError: UiText? = null,
) {
    val isValid: Boolean
        get() = isLevelValid &&
            isSubjectValid &&
            isCgpaValid &&
            isTestScoreValid &&
            isAgeValid &&
            isExperienceValid &&
            isBudgetValid &&
            isCountriesValid
}

class ValidateProfileFieldsUseCase @Inject constructor() {

    fun validate(
        level: String,
        subject: String,
        cgpaInput: String,
        cgpaScale: Double,
        testType: TestType,
        testScoreInput: String,
        experienceYearsInput: String,
        ageInput: String,
        budgetRange: String,
        selectedCountries: List<String>,
    ): ProfileValidationResult {
        // Level validation
        val levelValid = level.isNotBlank()
        val levelErr = if (!levelValid) UiText.StringResource(R.string.validation_level_required) else null

        // Subject validation
        val subjectValid = subject.isNotBlank()
        val subjectErr = if (!subjectValid) UiText.StringResource(R.string.validation_subject_required) else null

        // CGPA validation
        val cgpaDouble = cgpaInput.toDoubleOrNull()
        val cgpaValid = cgpaDouble != null && cgpaDouble >= 0.0 && cgpaDouble <= cgpaScale
        val cgpaScaleString = if (cgpaScale == 5.0) "5.0" else "4.0"
        val cgpaErr = if (!cgpaValid) {
            UiText.StringResource(R.string.validation_cgpa_invalid, cgpaScaleString)
        } else {
            null
        }

        // Test Score validation (IELTS: 0 to 9 in steps of 0.5)
        var testScoreValid = true
        var testScoreErr: UiText? = null
        if (testType == TestType.IELTS && testScoreInput.isNotBlank()) {
            val scoreDouble = testScoreInput.toDoubleOrNull()
            val isStepOfHalf = scoreDouble != null && isStepOfHalf(scoreDouble)
            if (scoreDouble == null || scoreDouble < 0.0 || scoreDouble > 9.0 || !isStepOfHalf) {
                testScoreValid = false
                testScoreErr = UiText.StringResource(R.string.validation_test_score_invalid)
            }
        }

        // Age validation (15 to 60)
        val ageInt = ageInput.toIntOrNull()
        val ageValid = ageInt != null && ageInt in 15..60
        val ageErr = if (!ageValid) UiText.StringResource(R.string.validation_age_invalid) else null

        // Experience validation (0 to 40)
        val expInt = experienceYearsInput.toIntOrNull()
        val expValid = expInt != null && expInt in 0..40
        val expErr = if (!expValid) UiText.StringResource(R.string.validation_experience_invalid) else null

        // Budget validation
        val budgetValid = budgetRange.isNotBlank()
        val budgetErr = if (!budgetValid) UiText.StringResource(R.string.validation_budget_required) else null

        // Target countries validation
        val countriesValid = selectedCountries.isNotEmpty()
        val countriesErr = if (!countriesValid) UiText.StringResource(R.string.validation_countries_required) else null

        return ProfileValidationResult(
            isLevelValid = levelValid,
            levelError = levelErr,
            isSubjectValid = subjectValid,
            subjectError = subjectErr,
            isCgpaValid = cgpaValid,
            cgpaError = cgpaErr,
            isTestScoreValid = testScoreValid,
            testScoreError = testScoreErr,
            isAgeValid = ageValid,
            ageError = ageErr,
            isExperienceValid = expValid,
            experienceError = expErr,
            isBudgetValid = budgetValid,
            budgetError = budgetErr,
            isCountriesValid = countriesValid,
            countriesError = countriesErr,
        )
    }

    private fun isStepOfHalf(value: Double): Boolean {
        val remainder = abs(value * 2.0 - (value * 2.0).toInt())
        return remainder < 1e-6 || remainder > (1.0 - 1e-6)
    }
}
