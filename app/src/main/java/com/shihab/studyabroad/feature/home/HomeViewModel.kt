package com.shihab.studyabroad.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.domain.model.CountryTier
import com.shihab.studyabroad.domain.model.FundingType
import com.shihab.studyabroad.domain.model.ScholarshipStatus
import com.shihab.studyabroad.domain.repository.CountryRepository
import com.shihab.studyabroad.domain.repository.ScholarshipRepository
import com.shihab.studyabroad.domain.usecase.GetScholarshipStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    scholarshipRepository: ScholarshipRepository,
    countryRepository: CountryRepository,
    private val getScholarshipStatusUseCase: GetScholarshipStatusUseCase,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        scholarshipRepository.getScholarships(),
        countryRepository.getCountries(),
    ) { scholarships, countries ->
        if (scholarships.isEmpty() && countries.isEmpty()) {
            HomeUiState.Empty
        } else {
            val fullyFunded = scholarships
                .filter { it.funding == FundingType.FULL }
                .take(3)

            val lowCostCountries = countries
                .filter { it.tier == CountryTier.SCHOLARSHIP }
                .take(3)

            val prFriendlyCountries = countries
                .filter { it.tier == CountryTier.PR_WORK }
                .take(3)

            val deadlineSoon = scholarships
                .filter { getScholarshipStatusUseCase(it) == ScholarshipStatus.OPEN }
                .sortedBy { it.deadline }
                .take(3)

            HomeUiState.Content(
                HomeData(
                    progressPercentage = 60,
                    topFullyFundedScholarships = fullyFunded,
                    topLowCostCountries = lowCostCountries,
                    topPrFriendlyCountries = prFriendlyCountries,
                    deadlineSoonScholarships = deadlineSoon,
                ),
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading,
    )
}
