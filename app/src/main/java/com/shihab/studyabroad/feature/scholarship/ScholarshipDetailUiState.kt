package com.shihab.studyabroad.feature.scholarship

import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.Scholarship
import com.shihab.studyabroad.domain.model.ScholarshipStatus

data class ScholarshipDetailData(
    val scholarship: Scholarship,
    val status: ScholarshipStatus,
    val expectedNextCycle: String? = null,
    val isStale: Boolean = false,
    val reminderDays: Int? = null,
    val reminderConfirmationMessage: String? = null,
)

sealed interface ScholarshipDetailUiState {
    data object Loading : ScholarshipDetailUiState
    data class Content(val data: ScholarshipDetailData) : ScholarshipDetailUiState
    data object Empty : ScholarshipDetailUiState
    data class Error(val message: UiText) : ScholarshipDetailUiState
    data class OfflineStale(val data: ScholarshipDetailData, val message: UiText) : ScholarshipDetailUiState
}
