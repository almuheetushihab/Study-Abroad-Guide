package com.shihab.studyabroad.domain.repository

/**
 * Domain interface for clearing all user-related local data across Room tables and DataStore.
 */
interface LocalDataCleaner {
    suspend fun clearAllLocalData()
}
