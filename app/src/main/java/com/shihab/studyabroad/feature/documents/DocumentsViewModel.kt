package com.shihab.studyabroad.feature.documents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.core.common.UrlOpener
import com.shihab.studyabroad.domain.usecase.ObserveChecklistProgressUseCase
import com.shihab.studyabroad.domain.usecase.ToggleChecklistItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentsViewModel @Inject constructor(
    observeChecklistProgressUseCase: ObserveChecklistProgressUseCase,
    private val toggleChecklistItemUseCase: ToggleChecklistItemUseCase,
    val urlOpener: UrlOpener,
) : ViewModel() {

    val uiState: StateFlow<DocumentsUiState> = observeChecklistProgressUseCase()
        .map { progressData ->
            if (progressData.items.isEmpty()) {
                DocumentsUiState.Empty
            } else {
                DocumentsUiState.Content(
                    DocumentsData(
                        items = progressData.items,
                        completedCount = progressData.completedCount,
                        totalCount = progressData.totalCount,
                        progressPercentage = progressData.progressPercentage,
                    ),
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DocumentsUiState.Loading,
        )

    fun onToggleItem(itemId: String) {
        viewModelScope.launch {
            toggleChecklistItemUseCase(itemId)
        }
    }

    fun openMoFAPortal() {
        urlOpener.openUrl("https://www.mfa.gov.bd")
    }
}
