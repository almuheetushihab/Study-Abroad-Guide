package com.shihab.studyabroad.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState.Content())
    val uiState: StateFlow<OnboardingUiState.Content> = _uiState.asStateFlow()

    fun onNextPage() {
        val current = _uiState.value.currentPage
        if (current < _uiState.value.totalPages - 1) {
            _uiState.value = _uiState.value.copy(currentPage = current + 1)
        } else {
            completeOnboarding()
        }
    }

    fun onPreviousPage() {
        val current = _uiState.value.currentPage
        if (current > 0) {
            _uiState.value = _uiState.value.copy(currentPage = current - 1)
        }
    }

    fun onSkip() {
        completeOnboarding()
    }

    private fun completeOnboarding() {
        viewModelScope.launch {
            userPreferencesRepository.setOnboardingDone(true)
            _uiState.value = _uiState.value.copy(isCompleted = true)
        }
    }
}
