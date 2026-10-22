package com.yourname.studyabroad.data.repository

import com.yourname.studyabroad.domain.model.Scholarship
import com.yourname.studyabroad.domain.repository.ScholarshipRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeScholarshipRepository
@Inject
constructor() : ScholarshipRepository {
    private val scholarshipsFlow = MutableStateFlow(SampleData.sampleScholarships)

    override fun getScholarships(): Flow<List<Scholarship>> = scholarshipsFlow.asStateFlow()

    override fun getScholarshipById(id: String): Flow<Scholarship?> = scholarshipsFlow.map { list -> list.find { it.id == id } }

    override fun getScholarshipsByCountry(countryId: String): Flow<List<Scholarship>> = scholarshipsFlow.map { list ->
        list.filter {
            it.countryId == countryId
        }
    }
}
