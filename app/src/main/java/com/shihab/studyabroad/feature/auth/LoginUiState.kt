package com.shihab.studyabroad.feature.auth

import com.shihab.studyabroad.core.ui.UiText

enum class LoginStep {
    PHONE_ENTRY,
    OTP_ENTRY,
}

sealed interface LoginUiState {
    data object Loading : LoginUiState
    data class Content(
        val step: LoginStep = LoginStep.PHONE_ENTRY,
        val phoneNumber: String = "",
        val otpCode: String = "",
        val isLoading: Boolean = false,
        val errorMessage: UiText? = null,
        val isSignedIn: Boolean = false,
    ) : LoginUiState
    data class Error(val message: UiText) : LoginUiState
}
