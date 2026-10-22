package com.yourname.studyabroad.domain.repository

import com.yourname.studyabroad.domain.model.University
import kotlinx.coroutines.flow.Flow

interface UniversityRepository {
    fun getUniversities(): Flow<List<University>>

    fun getUniversitiesByCountry(countryId: String): Flow<List<University>>
}
