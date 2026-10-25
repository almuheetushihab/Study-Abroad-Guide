package com.shihab.studyabroad.feature.scholarship

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.core.common.AnalyticsTracker
import com.shihab.studyabroad.domain.model.FundingType
import com.shihab.studyabroad.domain.model.ScholarshipStatus
import com.shihab.studyabroad.domain.repository.ScholarshipRepository
import com.shihab.studyabroad.domain.usecase.GetScholarshipStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ScholarshipListViewModel @Inject constructor(
    scholarshipRepository: ScholarshipRepository,
    private val getScholarshipStatusUseCase: GetScholarshipStatusUseCase,
    private val analyticsTracker: AnalyticsTracker,
) : ViewModel() {

    private val countryFilter = MutableStateFlow("All")
    private val levelFilter = MutableStateFlow("All")
    private val fundingFilter = MutableStateFlow("All")
    private val statusFilter = MutableStateFlow("All")

    val uiState: StateFlow<ScholarshipListUiState> = combine(
        scholarshipRepository.getScholarships(),
        countryFilter,
        levelFilter,
        fundingFilter,
        statusFilter,
    ) { scholarships, country, level, funding, status ->
        val itemsWithStatus = scholarships.map { scholarship ->
            ScholarshipItemState(
                scholarship = scholarship,
                status = getScholarshipStatusUseCase(scholarship),
            )
        }

        val filtered = filterScholarships(itemsWithStatus, country, level, funding, status)

        if (filtered.isEmpty()) {
            ScholarshipListUiState.Empty
        } else {
            ScholarshipListUiState.Content(
                ScholarshipListData(
                    selectedCountryFilter = country,
                    selectedLevelFilter = level,
                    selectedFundingFilter = funding,
                    selectedStatusFilter = status,
                    items = filtered,
                ),
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ScholarshipListUiState.Loading,
    )

    fun onSelectCountryFilter(country: String) {
        countryFilter.value = country
        analyticsTracker.logEvent("search_performed", mapOf("area" to "scholarships"))
    }

    fun onSelectLevelFilter(level: String) {
        levelFilter.value = level
        analyticsTracker.logEvent("search_performed", mapOf("area" to "scholarships"))
    }

    fun onSelectFundingFilter(funding: String) {
        fundingFilter.value = funding
        analyticsTracker.logEvent("search_performed", mapOf("area" to "scholarships"))
    }

    fun onSelectStatusFilter(status: String) {
        statusFilter.value = status
        analyticsTracker.logEvent("search_performed", mapOf("area" to "scholarships"))
    }

    private fun filterScholarships(
        list: List<ScholarshipItemState>,
        country: String,
        level: String,
        funding: String,
        status: String,
    ): List<ScholarshipItemState> = list.filter { item ->
        val matchesCountry = when {
            country == "All" -> true
            country.contains("Germany", ignoreCase = true) -> item.scholarship.countryId.equals("de", ignoreCase = true)
            country.contains("Canada", ignoreCase = true) -> item.scholarship.countryId.equals("ca", ignoreCase = true)
            country.contains("Japan", ignoreCase = true) -> item.scholarship.countryId.equals("jp", ignoreCase = true)
            country.contains("Hungary", ignoreCase = true) -> item.scholarship.countryId.equals("hu", ignoreCase = true)
            country.contains("Turkey", ignoreCase = true) -> item.scholarship.countryId.equals("tr", ignoreCase = true)
            else -> item.scholarship.countryId.equals(country, ignoreCase = true)
        }

        val matchesLevel = level == "All" || item.scholarship.level.any { it.equals(level, ignoreCase = true) }
        val matchesFunding = when {
            funding.equals("Full", ignoreCase = true) -> item.scholarship.funding == FundingType.FULL
            funding.equals("Partial", ignoreCase = true) -> item.scholarship.funding == FundingType.PARTIAL
            funding.equals("Tuition Waiver", ignoreCase = true) -> item.scholarship.funding == FundingType.TUITION_WAIVER
            else -> true
        }
        val matchesStatus = when {
            status.equals("Open", ignoreCase = true) -> item.status == ScholarshipStatus.OPEN
            status.contains("Soon", ignoreCase = true) -> item.status == ScholarshipStatus.OPENING_SOON
            status.equals("Closed", ignoreCase = true) -> item.status == ScholarshipStatus.CLOSED
            else -> true
        }

        matchesCountry && matchesLevel && matchesFunding && matchesStatus
    }
}
