package com.shihab.studyabroad.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.core.common.DispatcherProvider
import com.shihab.studyabroad.data.local.AppDatabase
import com.shihab.studyabroad.domain.repository.LocalDataCleaner
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataCleanerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val appDatabase: AppDatabase,
    private val dispatcherProvider: DispatcherProvider,
) : LocalDataCleaner {

    override suspend fun clearAllLocalData(): AppResult<Unit> = withContext(dispatcherProvider.io) {
        try {
            appDatabase.clearAllTables()
            dataStore.edit { preferences ->
                preferences.clear()
            }
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(e, e.message ?: "Failed to clear local data")
        }
    }
}
