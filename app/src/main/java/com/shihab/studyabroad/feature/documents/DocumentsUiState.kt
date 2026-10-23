package com.shihab.studyabroad.feature.documents

import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.ChecklistItem

data class DocumentsData(
    val items: List<ChecklistItem> = emptyList(),
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val progressPercentage: Int = 0,
)

sealed interface DocumentsUiState {
    data object Loading : DocumentsUiState
    data class Content(val data: DocumentsData) : DocumentsUiState
    data object Empty : DocumentsUiState
    data class Error(val message: UiText) : DocumentsUiState
    data class OfflineStale(val data: DocumentsData, val message: UiText) : DocumentsUiState
}
