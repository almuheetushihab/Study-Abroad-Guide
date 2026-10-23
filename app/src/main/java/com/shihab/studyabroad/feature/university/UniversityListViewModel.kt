package com.shihab.studyabroad.feature.university

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.domain.model.University
import com.shihab.studyabroad.domain.repository.UniversityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class UniversityListViewModel @Inject constructor(universityRepository: UniversityRepository) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val countryFilter = MutableStateFlow("All")
    private val levelFilter = MutableStateFlow("All")
    private val scholarshipOnly = MutableStateFlow(false)

    val uiState: StateFlow<UniversityListUiState> = combine(
        universityRepository.getUniversities(),
        searchQuery,
        countryFilter,
        levelFilter,
        scholarshipOnly,
    ) { universities, query, country, level, scholOnly ->
        val filtered = filterUniversities(universities, query, country, level, scholOnly)
        if (filtered.isEmpty()) {
            UniversityListUiState.Empty
        } else {
            UniversityListUiState.Content(
                UniversityListData(
                    searchQuery = query,
                    selectedCountryFilter = country,
                    selectedLevelFilter = level,
                    scholarshipOnlyFilter = scholOnly,
                    universities = filtered,
                ),
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UniversityListUiState.Loading,
    )

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun onSelectCountryFilter(country: String) {
        countryFilter.value = country
    }

    fun onSelectLevelFilter(level: String) {
        levelFilter.value = level
    }

    fun onToggleScholarshipOnly() {
        scholarshipOnly.value = !scholarshipOnly.value
    }

    private fun filterUniversities(
        list: List<University>,
        query: String,
        country: String,
        level: String,
        scholOnly: Boolean,
    ): List<University> = list.filter { item ->
        val matchesQuery = query.isBlank() || item.name.contains(query, ignoreCase = true)
        val matchesCountry = country == "All" || item.countryId.equals(country.take(2), ignoreCase = true)
        val matchesLevel = level == "All" || item.level.any { it.equals(level, ignoreCase = true) }
        val matchesSchol = !scholOnly || item.hasScholarship
        matchesQuery && matchesCountry && matchesLevel && matchesSchol
    }
}
