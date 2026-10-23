package com.shihab.studyabroad.feature.country

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.core.common.UrlOpener
import com.shihab.studyabroad.domain.repository.CountryRepository
import com.shihab.studyabroad.domain.repository.ScholarshipRepository
import com.shihab.studyabroad.domain.repository.UniversityRepository
import com.shihab.studyabroad.domain.usecase.IsRecordStaleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CountryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    countryRepository: CountryRepository,
    scholarshipRepository: ScholarshipRepository,
    universityRepository: UniversityRepository,
    private val isRecordStaleUseCase: IsRecordStaleUseCase,
    val urlOpener: UrlOpener,
) : ViewModel() {

    private val countryId: String = checkNotNull(savedStateHandle["id"])

    val uiState: StateFlow<CountryDetailUiState> = combine(
        countryRepository.getCountryById(countryId),
        scholarshipRepository.getScholarshipsByCountry(countryId),
        universityRepository.getUniversitiesByCountry(countryId),
    ) { country, scholarships, universities ->
        if (country == null) {
            CountryDetailUiState.Empty
        } else {
            val isStale = isRecordStaleUseCase(country.verifiedOn)
            CountryDetailUiState.Content(
                CountryDetailData(
                    country = country,
                    scholarships = scholarships,
                    universities = universities,
                    isStale = isStale,
                ),
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CountryDetailUiState.Loading,
    )

    fun openSourceUrl(url: String) {
        urlOpener.openUrl(url)
    }
}
