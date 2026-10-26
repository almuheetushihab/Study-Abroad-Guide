package com.shihab.studyabroad.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.shihab.studyabroad.data.local.AppDatabase
import com.shihab.studyabroad.domain.repository.LocalDataCleaner
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "study_abroad_preferences")

@Singleton
class LocalDataCleanerImpl @Inject constructor(@ApplicationContext private val context: Context, private val appDatabase: AppDatabase) :
    LocalDataCleaner {

    override suspend fun clearAllLocalData() {
        try {
            appDatabase.clearAllTables()
        } catch (_: Exception) {
            // Best effort database clear
        }
        context.preferencesDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
