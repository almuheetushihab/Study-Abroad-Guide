package com.shihab.studyabroad.domain.repository

import com.shihab.studyabroad.domain.model.Country
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    fun getCountries(): Flow<List<Country>>
    fun getCountryById(id: String): Flow<Country?>
}
