package com.shihab.studyabroad.feature.university

import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.University
import com.shihab.studyabroad.domain.usecase.RequirementComparisonResult

data class UniversityDetailData(
    val university: University,
    val requirementComparison: RequirementComparisonResult,
    val isStale: Boolean = false,
    val isSaved: Boolean = false,
)

sealed interface UniversityDetailUiState {
    data object Loading : UniversityDetailUiState
    data class Content(val data: UniversityDetailData) : UniversityDetailUiState
    data object Empty : UniversityDetailUiState
    data class Error(val message: UiText) : UniversityDetailUiState
    data class OfflineStale(val data: UniversityDetailData, val message: UiText) : UniversityDetailUiState
}
