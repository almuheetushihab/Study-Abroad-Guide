package com.shihab.studyabroad.feature.profile

import com.shihab.studyabroad.domain.model.UserProfile

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Content(val userProfile: UserProfile?, val appVersion: String, val isDeleteSuccess: Boolean = false) : ProfileUiState
}
