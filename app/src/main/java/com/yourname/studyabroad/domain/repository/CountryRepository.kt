package com.yourname.studyabroad.domain.repository

import com.yourname.studyabroad.domain.model.Country
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    fun getCountries(): Flow<List<Country>>

    fun getCountryById(id: String): Flow<Country?>
}
