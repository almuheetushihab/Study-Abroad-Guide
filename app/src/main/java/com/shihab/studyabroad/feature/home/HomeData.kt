package com.shihab.studyabroad.feature.home

import com.shihab.studyabroad.domain.model.Country
import com.shihab.studyabroad.domain.model.Scholarship

data class HomeData(
    val progressPercentage: Int = 60,
    val topFullyFundedScholarships: List<Scholarship> = emptyList(),
    val topLowCostCountries: List<Country> = emptyList(),
    val topPrFriendlyCountries: List<Country> = emptyList(),
    val deadlineSoonScholarships: List<Scholarship> = emptyList(),
)
