package com.shihab.studyabroad.feature.country

import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.Country

data class CountryListData(
    val selectedTierFilter: String = "All",
    val availableTiers: List<String> = listOf("All", "Tier 1 (Scholarship)", "Tier 2 (PR / Work)"),
    val countries: List<Country> = emptyList(),
)

sealed interface CountryListUiState {
    data object Loading : CountryListUiState
    data class Content(val data: CountryListData) : CountryListUiState
    data object Empty : CountryListUiState
    data class Error(val message: UiText) : CountryListUiState
    data class OfflineStale(val data: CountryListData, val message: UiText) : CountryListUiState
}
