package com.yourname.studyabroad.domain.repository

import com.yourname.studyabroad.domain.model.Scholarship
import kotlinx.coroutines.flow.Flow

interface ScholarshipRepository {
    fun getScholarships(): Flow<List<Scholarship>>

    fun getScholarshipById(id: String): Flow<Scholarship?>

    fun getScholarshipsByCountry(countryId: String): Flow<List<Scholarship>>
}
