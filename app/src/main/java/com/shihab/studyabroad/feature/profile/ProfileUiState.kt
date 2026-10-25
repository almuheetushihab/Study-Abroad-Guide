package com.shihab.studyabroad.feature.profile

import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.UserProfile

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Content(
        val userProfile: UserProfile?,
        val appVersion: String,
        val isDeleteSuccess: Boolean = false,
        val errorMessage: UiText? = null,
    ) : ProfileUiState
}
