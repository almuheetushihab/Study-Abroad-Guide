package com.shihab.studyabroad.feature.profilesetup

import com.shihab.studyabroad.domain.model.Country
import com.shihab.studyabroad.domain.model.FundingGoal
import com.shihab.studyabroad.domain.model.TestType
import com.shihab.studyabroad.domain.usecase.ProfileValidationResult

data class ProfileFormState(
    val level: String = "Bachelor",
    val subject: String = "Computer Science",
    val cgpa: String = "3.50",
    val cgpaScale: Double = 4.0,
    val testType: TestType = TestType.IELTS,
    val testScore: String = "7.0",
    val experienceYears: String = "1",
    val age: String = "24",
    val budgetRange: String = "10-20 Lakh BDT",
    val goal: FundingGoal = FundingGoal.SCHOLARSHIP,
    val selectedCountryIds: List<String> = listOf("de", "hu"),
)

sealed interface ProfileSetupUiState {
    data class Content(
        val currentStep: Int = 1,
        val totalSteps: Int = 4,
        val formState: ProfileFormState = ProfileFormState(),
        val validationResult: ProfileValidationResult = ProfileValidationResult(),
        val availableCountries: List<Country> = emptyList(),
        val isEditMode: Boolean = false,
        val isLoading: Boolean = false,
        val isSaved: Boolean = false,
    ) : ProfileSetupUiState
}
