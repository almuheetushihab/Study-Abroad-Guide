package com.shihab.studyabroad.feature.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.domain.model.SavedType
import com.shihab.studyabroad.domain.repository.SavedRepository
import com.shihab.studyabroad.domain.usecase.ObserveSavedItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    observeSavedItemsUseCase: ObserveSavedItemsUseCase,
    private val savedRepository: SavedRepository,
) : ViewModel() {

    val uiState: StateFlow<SavedUiState> = observeSavedItemsUseCase()
        .map { populatedSavedItems ->
            if (populatedSavedItems.isEmpty) {
                SavedUiState.Empty
            } else {
                SavedUiState.Content(populatedSavedItems)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SavedUiState.Loading,
        )

    fun unsaveItem(type: SavedType, itemId: String) {
        viewModelScope.launch {
            savedRepository.unsaveItem(type, itemId)
        }
    }

    fun restoreItem(type: SavedType, itemId: String) {
        viewModelScope.launch {
            savedRepository.saveItem(type, itemId)
        }
    }
}
