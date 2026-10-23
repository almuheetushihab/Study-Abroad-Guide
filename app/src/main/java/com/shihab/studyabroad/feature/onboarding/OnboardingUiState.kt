package com.shihab.studyabroad.feature.onboarding

sealed interface OnboardingUiState {
    data class Content(val currentPage: Int = 0, val totalPages: Int = 3, val isCompleted: Boolean = false) : OnboardingUiState
}
