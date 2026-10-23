package com.shihab.studyabroad.feature.university

import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.University

data class UniversityListData(
    val searchQuery: String = "",
    val selectedCountryFilter: String = "All",
    val selectedLevelFilter: String = "All",
    val scholarshipOnlyFilter: Boolean = false,
    val availableCountries: List<String> = listOf("All", "Germany", "Canada", "Japan", "Hungary", "Turkey"),
    val availableLevels: List<String> = listOf("All", "BSc", "MSc", "PhD"),
    val universities: List<University> = emptyList(),
)

sealed interface UniversityListUiState {
    data object Loading : UniversityListUiState
    data class Content(val data: UniversityListData) : UniversityListUiState
    data object Empty : UniversityListUiState
    data class Error(val message: UiText) : UniversityListUiState
    data class OfflineStale(val data: UniversityListData, val message: UiText) : UniversityListUiState
}
