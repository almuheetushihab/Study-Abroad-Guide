package com.shihab.studyabroad.data.repository

import com.shihab.studyabroad.domain.model.University
import com.shihab.studyabroad.domain.repository.UniversityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeUniversityRepository @Inject constructor() : UniversityRepository {

    private val universitiesFlow = MutableStateFlow(SampleData.sampleUniversities)

    override fun getUniversities(): Flow<List<University>> = universitiesFlow.asStateFlow()

    override fun getUniversitiesByCountry(countryId: String): Flow<List<University>> = universitiesFlow.map { list ->
        list.filter { it.countryId == countryId }
    }
}
