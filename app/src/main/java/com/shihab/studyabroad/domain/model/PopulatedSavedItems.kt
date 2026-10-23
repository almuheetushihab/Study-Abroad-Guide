package com.shihab.studyabroad.domain.model

data class PopulatedSavedItems(
    val countries: List<Country> = emptyList(),
    val universities: List<University> = emptyList(),
    val scholarships: List<Scholarship> = emptyList(),
) {
    val isEmpty: Boolean
        get() = countries.isEmpty() && universities.isEmpty() && scholarships.isEmpty()
}
