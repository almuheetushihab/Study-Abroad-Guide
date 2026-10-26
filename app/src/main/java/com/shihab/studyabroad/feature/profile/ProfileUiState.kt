package com.shihab.studyabroad.feature.profile

import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.AuthProvider
import com.shihab.studyabroad.domain.model.UserProfile

sealed interface ProfileUiState {
    data object Loading : ProfileUiState

    data class Content(
        val userProfile: UserProfile?,
        val appVersion: String,
        val userPhone: String = "",
        val isProcessing: Boolean = false,
        val showSignOutDialog: Boolean = false,
        val showDeleteDialog: Boolean = false,
        val showReauthDialog: Boolean = false,
        val reauthProvider: AuthProvider? = null,
        val showOtpDialog: Boolean = false,
        val otpCode: String = "",
        val otpResendSeconds: Int = 0,
        val errorMessage: UiText? = null,
    ) : ProfileUiState
}
