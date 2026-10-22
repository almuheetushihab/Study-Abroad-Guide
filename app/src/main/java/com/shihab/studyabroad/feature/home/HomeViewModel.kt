package com.shihab.studyabroad.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.domain.repository.CountryRepository
import com.shihab.studyabroad.domain.repository.ScholarshipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(scholarshipRepository: ScholarshipRepository, countryRepository: CountryRepository) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        scholarshipRepository.getScholarships(),
        countryRepository.getCountries(),
    ) { scholarships, countries ->
        if (scholarships.isEmpty() && countries.isEmpty()) {
            HomeUiState.Empty
        } else {
            HomeUiState.Content(
                HomeData(
                    featuredScholarships = scholarships,
                    topCountries = countries,
                ),
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading,
    )
}
