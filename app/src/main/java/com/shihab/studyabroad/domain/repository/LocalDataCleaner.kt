package com.shihab.studyabroad.domain.repository

import com.shihab.studyabroad.core.common.AppResult

/**
 * Domain interface for clearing all user-related local data across Room tables and DataStore.
 */
interface LocalDataCleaner {
    suspend fun clearAllLocalData(): AppResult<Unit>
}
