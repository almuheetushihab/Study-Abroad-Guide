package com.shihab.studyabroad.domain.repository

import com.shihab.studyabroad.domain.model.Scholarship
import kotlinx.coroutines.flow.Flow

interface ScholarshipRepository {
    fun getScholarships(): Flow<List<Scholarship>>
    fun getScholarshipById(id: String): Flow<Scholarship?>
    fun getScholarshipsByCountry(countryId: String): Flow<List<Scholarship>>
}
