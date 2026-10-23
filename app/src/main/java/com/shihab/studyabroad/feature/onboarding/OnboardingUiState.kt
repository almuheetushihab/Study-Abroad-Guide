package com.shihab.studyabroad.feature.onboarding

import com.shihab.studyabroad.core.ui.UiText

sealed interface OnboardingUiState {
    data object Loading : OnboardingUiState
    data class Content(val currentPage: Int = 0, val totalPages: Int = 3, val isCompleted: Boolean = false) : OnboardingUiState
    data class Error(val message: UiText) : OnboardingUiState
}
