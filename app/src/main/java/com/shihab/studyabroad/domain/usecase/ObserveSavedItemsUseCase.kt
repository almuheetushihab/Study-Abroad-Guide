package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.domain.model.PopulatedSavedItems
import com.shihab.studyabroad.domain.model.SavedType
import com.shihab.studyabroad.domain.repository.CountryRepository
import com.shihab.studyabroad.domain.repository.SavedRepository
import com.shihab.studyabroad.domain.repository.ScholarshipRepository
import com.shihab.studyabroad.domain.repository.UniversityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveSavedItemsUseCase @Inject constructor(
    private val savedRepository: SavedRepository,
    private val countryRepository: CountryRepository,
    private val universityRepository: UniversityRepository,
    private val scholarshipRepository: ScholarshipRepository,
) {
    operator fun invoke(): Flow<PopulatedSavedItems> = combine(
        savedRepository.observeSavedItems(),
        countryRepository.getCountries(),
        universityRepository.getUniversities(),
        scholarshipRepository.getScholarships(),
    ) { savedItems, countries, universities, scholarships ->
        val countryMap = countries.associateBy { it.id }
        val universityMap = universities.associateBy { it.id }
        val scholarshipMap = scholarships.associateBy { it.id }

        val savedCountries = savedItems
            .filter { it.type == SavedType.COUNTRY }
            .mapNotNull { countryMap[it.itemId] }

        val savedUniversities = savedItems
            .filter { it.type == SavedType.UNIVERSITY }
            .mapNotNull { universityMap[it.itemId] }

        val savedScholarships = savedItems
            .filter { it.type == SavedType.SCHOLARSHIP }
            .mapNotNull { scholarshipMap[it.itemId] }

        PopulatedSavedItems(
            countries = savedCountries,
            universities = savedUniversities,
            scholarships = savedScholarships,
        )
    }
}
