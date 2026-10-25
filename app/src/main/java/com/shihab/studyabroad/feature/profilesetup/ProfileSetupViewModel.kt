package com.shihab.studyabroad.feature.profilesetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.core.common.AnalyticsTracker
import com.shihab.studyabroad.domain.model.FundingGoal
import com.shihab.studyabroad.domain.model.TestType
import com.shihab.studyabroad.domain.model.UserProfile
import com.shihab.studyabroad.domain.repository.CountryRepository
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import com.shihab.studyabroad.domain.usecase.ValidateProfileFieldsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val countryRepository: CountryRepository,
    private val validateProfileFieldsUseCase: ValidateProfileFieldsUseCase,
    private val analyticsTracker: AnalyticsTracker,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSetupUiState.Content())
    val uiState: StateFlow<ProfileSetupUiState.Content> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    fun initEditMode(isEditMode: Boolean) {
        _uiState.value = _uiState.value.copy(isEditMode = isEditMode)
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val countries = countryRepository.getCountries().firstOrNull() ?: emptyList()
            val existingProfile = userPreferencesRepository.userProfile.firstOrNull()

            val formState = if (existingProfile != null && existingProfile.level.isNotBlank()) {
                ProfileFormState(
                    level = existingProfile.level,
                    subject = existingProfile.subject,
                    cgpa = existingProfile.cgpa?.toString() ?: "",
                    cgpaScale = existingProfile.cgpaScale,
                    testType = existingProfile.testType,
                    testScore = existingProfile.testScore?.toString() ?: "",
                    experienceYears = existingProfile.experienceYears.toString(),
                    age = existingProfile.age?.toString() ?: "",
                    budgetRange = existingProfile.budgetRange,
                    goal = existingProfile.goal,
                    selectedCountryIds = existingProfile.targetCountries,
                )
            } else {
                ProfileFormState()
            }

            _uiState.value = _uiState.value.copy(
                formState = formState,
                availableCountries = countries,
            )
        }
    }

    fun onLevelChanged(level: String) {
        _uiState.value = _uiState.value.copy(
            formState = _uiState.value.formState.copy(level = level),
        )
    }

    fun onSubjectChanged(subject: String) {
        _uiState.value = _uiState.value.copy(
            formState = _uiState.value.formState.copy(subject = subject),
        )
    }

    fun onCgpaChanged(cgpa: String) {
        _uiState.value = _uiState.value.copy(
            formState = _uiState.value.formState.copy(cgpa = cgpa),
        )
    }

    fun onCgpaScaleChanged(scale: Double) {
        _uiState.value = _uiState.value.copy(
            formState = _uiState.value.formState.copy(cgpaScale = scale),
        )
    }

    fun onTestTypeChanged(testType: TestType) {
        _uiState.value = _uiState.value.copy(
            formState = _uiState.value.formState.copy(testType = testType),
        )
    }

    fun onTestScoreChanged(score: String) {
        _uiState.value = _uiState.value.copy(
            formState = _uiState.value.formState.copy(testScore = score),
        )
    }

    fun onExperienceYearsChanged(exp: String) {
        _uiState.value = _uiState.value.copy(
            formState = _uiState.value.formState.copy(experienceYears = exp),
        )
    }

    fun onAgeChanged(age: String) {
        _uiState.value = _uiState.value.copy(
            formState = _uiState.value.formState.copy(age = age),
        )
    }

    fun onBudgetRangeChanged(budget: String) {
        _uiState.value = _uiState.value.copy(
            formState = _uiState.value.formState.copy(budgetRange = budget),
        )
    }

    fun onGoalChanged(goal: FundingGoal) {
        _uiState.value = _uiState.value.copy(
            formState = _uiState.value.formState.copy(goal = goal),
        )
    }

    fun onCountryToggled(countryId: String) {
        val currentSelected = _uiState.value.formState.selectedCountryIds.toMutableList()
        if (currentSelected.contains(countryId)) {
            currentSelected.remove(countryId)
        } else {
            currentSelected.add(countryId)
        }
        _uiState.value = _uiState.value.copy(
            formState = _uiState.value.formState.copy(selectedCountryIds = currentSelected),
        )
    }

    fun onNextStep() {
        val currentState = _uiState.value
        val form = currentState.formState
        val validation = validateProfileFieldsUseCase.validate(
            level = form.level,
            subject = form.subject,
            cgpaInput = form.cgpa,
            cgpaScale = form.cgpaScale,
            testType = form.testType,
            testScoreInput = form.testScore,
            experienceYearsInput = form.experienceYears,
            ageInput = form.age,
            budgetRange = form.budgetRange,
            selectedCountries = form.selectedCountryIds,
        )

        _uiState.value = currentState.copy(validationResult = validation)

        // Validate step-specific requirements before advancing
        val stepValid = when (currentState.currentStep) {
            1 -> validation.isLevelValid && validation.isSubjectValid
            2 -> validation.isCgpaValid && validation.isTestScoreValid
            3 -> validation.isExperienceValid && validation.isAgeValid
            4 -> validation.isBudgetValid && validation.isCountriesValid
            else -> true
        }

        if (!stepValid) return

        if (currentState.currentStep < currentState.totalSteps) {
            _uiState.value = _uiState.value.copy(currentStep = currentState.currentStep + 1)
        } else {
            saveProfile()
        }
    }

    fun onPreviousStep() {
        val current = _uiState.value.currentStep
        if (current > 1) {
            _uiState.value = _uiState.value.copy(currentStep = current - 1)
        }
    }

    private fun saveProfile() {
        val form = _uiState.value.formState
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val currentTrack = userPreferencesRepository.selectedTrack.firstOrNull()
                ?: com.shihab.studyabroad.domain.model.Track.GRADUATE

            val profile = UserProfile(
                id = "user_1",
                name = "User Profile",
                targetTrack = currentTrack,
                level = form.level,
                subject = form.subject,
                cgpa = form.cgpa.toDoubleOrNull(),
                cgpaScale = form.cgpaScale,
                testType = form.testType,
                testScore = form.testScore.toDoubleOrNull(),
                experienceYears = form.experienceYears.toIntOrNull() ?: 0,
                age = form.age.toIntOrNull(),
                budgetRange = form.budgetRange,
                goal = form.goal,
                targetCountries = form.selectedCountryIds,
            )

            userPreferencesRepository.saveUserProfile(profile)
            analyticsTracker.logEvent("profile_completed", mapOf("track" to currentTrack.name))
            _uiState.value = _uiState.value.copy(isLoading = false, isSaved = true)
        }
    }
}
