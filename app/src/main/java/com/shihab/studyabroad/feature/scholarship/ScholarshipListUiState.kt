package com.shihab.studyabroad.feature.scholarship

import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.Scholarship
import com.shihab.studyabroad.domain.model.ScholarshipStatus

data class ScholarshipItemState(val scholarship: Scholarship, val status: ScholarshipStatus)

data class ScholarshipListData(
    val selectedCountryFilter: String = "All",
    val selectedLevelFilter: String = "All",
    val selectedFundingFilter: String = "All",
    val selectedStatusFilter: String = "All",
    val availableCountries: List<String> = listOf("All", "Germany", "Canada", "Japan", "Hungary", "Turkey"),
    val availableLevels: List<String> = listOf("All", "BSc", "MSc", "PhD"),
    val availableFundings: List<String> = listOf("All", "Full", "Partial", "Tuition Waiver"),
    val availableStatuses: List<String> = listOf("All", "Open", "Opening Soon", "Closed"),
    val items: List<ScholarshipItemState> = emptyList(),
)

sealed interface ScholarshipListUiState {
    data object Loading : ScholarshipListUiState
    data class Content(val data: ScholarshipListData) : ScholarshipListUiState
    data object Empty : ScholarshipListUiState
    data class Error(val message: UiText) : ScholarshipListUiState
    data class OfflineStale(val data: ScholarshipListData, val message: UiText) : ScholarshipListUiState
}
