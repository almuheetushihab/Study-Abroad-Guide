package com.shihab.studyabroad.feature.country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.domain.model.Country
import com.shihab.studyabroad.domain.repository.CountryRepository
import com.shihab.studyabroad.feature.country.components.ComparisonSortColumn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CountryComparisonViewModel @Inject constructor(countryRepository: CountryRepository) : ViewModel() {

    private val selectedIds = MutableStateFlow<Set<String>>(emptySet())
    private val sortColumn = MutableStateFlow(ComparisonSortColumn.NAME)

    val uiState: StateFlow<CountryComparisonUiState> = combine(
        countryRepository.getCountries(),
        selectedIds,
        sortColumn,
    ) { allCountries, selected, sort ->
        if (allCountries.isEmpty()) {
            CountryComparisonUiState.Empty
        } else {
            val initialSelected = if (selected.isEmpty()) {
                allCountries.take(3).map { it.id }.toSet()
            } else {
                selected
            }

            val filtered = allCountries.filter { initialSelected.contains(it.id) }
            val sorted = sortCountries(filtered, sort)

            CountryComparisonUiState.Content(
                CountryComparisonData(
                    availableCountries = allCountries,
                    selectedCountryIds = initialSelected,
                    comparedCountries = sorted,
                    sortColumn = sort,
                ),
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CountryComparisonUiState.Loading,
    )

    fun onToggleCountrySelection(countryId: String) {
        val current = selectedIds.value.toMutableSet()
        if (current.contains(countryId)) {
            current.remove(countryId)
        } else if (current.size < 3) {
            current.add(countryId)
        }
        selectedIds.value = current
    }

    fun onSortColumnClick(column: ComparisonSortColumn) {
        sortColumn.value = column
    }

    private fun sortCountries(list: List<Country>, column: ComparisonSortColumn): List<Country> = when (column) {
        ComparisonSortColumn.NAME -> list.sortedBy { it.name }
        ComparisonSortColumn.TIER -> list.sortedBy { it.tier }
        ComparisonSortColumn.TUITION -> list.sortedBy { it.tuitionRangeText }
        ComparisonSortColumn.LIVING -> list.sortedBy { it.livingRangeText }
    }
}
