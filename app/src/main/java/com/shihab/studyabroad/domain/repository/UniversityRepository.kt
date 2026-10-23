package com.shihab.studyabroad.domain.repository

import com.shihab.studyabroad.domain.model.University
import kotlinx.coroutines.flow.Flow

interface UniversityRepository {
    fun getUniversities(): Flow<List<University>>
    fun getUniversitiesByCountry(countryId: String): Flow<List<University>>
    fun getUniversityById(id: String): Flow<University?>
}
