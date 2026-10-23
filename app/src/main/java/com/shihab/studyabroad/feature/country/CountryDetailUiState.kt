package com.shihab.studyabroad.feature.country

import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.Country
import com.shihab.studyabroad.domain.model.Scholarship
import com.shihab.studyabroad.domain.model.University

data class CountryDetailData(
    val country: Country,
    val scholarships: List<Scholarship> = emptyList(),
    val universities: List<University> = emptyList(),
    val isStale: Boolean = false,
)

sealed interface CountryDetailUiState {
    data object Loading : CountryDetailUiState
    data class Content(val data: CountryDetailData) : CountryDetailUiState
    data object Empty : CountryDetailUiState
    data class Error(val message: UiText) : CountryDetailUiState
    data class OfflineStale(val data: CountryDetailData, val message: UiText) : CountryDetailUiState
}
