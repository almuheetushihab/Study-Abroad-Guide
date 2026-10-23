package com.shihab.studyabroad.feature.home

import com.shihab.studyabroad.core.ui.UiText

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Content(val data: HomeData) : HomeUiState
    data object Empty : HomeUiState
    data class Error(val message: UiText) : HomeUiState
    data class OfflineStale(val data: HomeData, val message: UiText) : HomeUiState
}
