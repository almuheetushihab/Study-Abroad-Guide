package com.shihab.studyabroad.feature.trackselect

import com.shihab.studyabroad.domain.model.Track

sealed interface TrackSelectUiState {
    data class Content(val selectedTrack: Track? = null, val isSaved: Boolean = false, val isLoading: Boolean = false) : TrackSelectUiState
}
