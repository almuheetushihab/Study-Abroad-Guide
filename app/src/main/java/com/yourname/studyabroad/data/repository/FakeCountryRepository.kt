package com.yourname.studyabroad.data.repository

import com.yourname.studyabroad.domain.model.Country
import com.yourname.studyabroad.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeCountryRepository
@Inject
constructor() : CountryRepository {
    private val countriesFlow = MutableStateFlow(SampleData.sampleCountries)

    override fun getCountries(): Flow<List<Country>> = countriesFlow.asStateFlow()

    override fun getCountryById(id: String): Flow<Country?> = countriesFlow.map { list -> list.find { it.id == id } }
}
