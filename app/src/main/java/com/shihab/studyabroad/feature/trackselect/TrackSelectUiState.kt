package com.shihab.studyabroad.feature.trackselect

import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.Track

sealed interface TrackSelectUiState {
    data object Loading : TrackSelectUiState
    data class Content(val selectedTrack: Track? = null, val isSaved: Boolean = false, val isLoading: Boolean = false) : TrackSelectUiState
    data class Error(val message: UiText) : TrackSelectUiState
}
