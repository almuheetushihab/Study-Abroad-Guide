package com.shihab.studyabroad.feature.saved

import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.PopulatedSavedItems

sealed interface SavedUiState {
    data object Loading : SavedUiState
    data class Content(val data: PopulatedSavedItems) : SavedUiState
    data object Empty : SavedUiState
    data class Error(val message: UiText) : SavedUiState
    data class OfflineStale(val data: PopulatedSavedItems, val message: UiText) : SavedUiState
}
