package com.shihab.studyabroad.feature.country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.domain.model.Country
import com.shihab.studyabroad.domain.model.CountryTier
import com.shihab.studyabroad.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CountryListViewModel @Inject constructor(countryRepository: CountryRepository) : ViewModel() {

    private val selectedTier = MutableStateFlow("All")

    val uiState: StateFlow<CountryListUiState> = combine(
        countryRepository.getCountries(),
        selectedTier,
    ) { countries, tierFilter ->
        val filtered = filterCountries(countries, tierFilter)
        if (filtered.isEmpty()) {
            CountryListUiState.Empty
        } else {
            CountryListUiState.Content(
                CountryListData(
                    selectedTierFilter = tierFilter,
                    countries = filtered,
                ),
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CountryListUiState.Loading,
    )

    fun onSelectTierFilter(tier: String) {
        selectedTier.value = tier
    }

    private fun filterCountries(list: List<Country>, tier: String): List<Country> = when {
        tier.contains("Tier 1") || tier.contains("Scholarship") -> list.filter { it.tier == CountryTier.SCHOLARSHIP }
        tier.contains("Tier 2") || tier.contains("PR") || tier.contains("Work") -> list.filter { it.tier == CountryTier.PR_WORK }
        else -> list
    }
}
