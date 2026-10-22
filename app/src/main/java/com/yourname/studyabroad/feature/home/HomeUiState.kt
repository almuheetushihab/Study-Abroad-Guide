package com.yourname.studyabroad.feature.home

import com.yourname.studyabroad.core.ui.UiText
import com.yourname.studyabroad.domain.model.Country
import com.yourname.studyabroad.domain.model.Scholarship

data class HomeData(val featuredScholarships: List<Scholarship>, val topCountries: List<Country>)

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Content(val data: HomeData) : HomeUiState

    data object Empty : HomeUiState

    data class Error(val message: UiText) : HomeUiState

    data class OfflineStale(val data: HomeData, val message: UiText) : HomeUiState
}
