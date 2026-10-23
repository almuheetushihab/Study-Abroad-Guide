package com.shihab.studyabroad.feature.trackselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.domain.model.Track
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackSelectViewModel @Inject constructor(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TrackSelectUiState.Content())
    val uiState: StateFlow<TrackSelectUiState.Content> = _uiState.asStateFlow()

    fun onSelectTrack(track: Track) {
        _uiState.value = _uiState.value.copy(selectedTrack = track)
    }

    fun onContinue() {
        val track = _uiState.value.selectedTrack ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            userPreferencesRepository.setSelectedTrack(track)
            _uiState.value = _uiState.value.copy(isLoading = false, isSaved = true)
        }
    }
}
