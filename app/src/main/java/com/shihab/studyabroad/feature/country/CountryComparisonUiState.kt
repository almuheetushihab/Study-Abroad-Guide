package com.shihab.studyabroad.feature.country

import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.Country
import com.shihab.studyabroad.feature.country.components.ComparisonSortColumn

data class CountryComparisonData(
    val availableCountries: List<Country> = emptyList(),
    val selectedCountryIds: Set<String> = emptySet(),
    val comparedCountries: List<Country> = emptyList(),
    val sortColumn: ComparisonSortColumn = ComparisonSortColumn.NAME,
)

sealed interface CountryComparisonUiState {
    data object Loading : CountryComparisonUiState
    data class Content(val data: CountryComparisonData) : CountryComparisonUiState
    data object Empty : CountryComparisonUiState
    data class Error(val message: UiText) : CountryComparisonUiState
    data class OfflineStale(val data: CountryComparisonData, val message: UiText) : CountryComparisonUiState
}
