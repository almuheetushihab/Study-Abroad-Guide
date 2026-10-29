package com.shihab.studyabroad.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.shihab.studyabroad.BuildConfig
import com.shihab.studyabroad.data.local.AppDatabase
import com.shihab.studyabroad.data.local.dao.ChecklistProgressDao
import com.shihab.studyabroad.data.local.dao.CountryDao
import com.shihab.studyabroad.data.local.dao.ReportDao
import com.shihab.studyabroad.data.local.dao.SavedItemDao
import com.shihab.studyabroad.data.local.dao.ScholarshipDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val USER_PREFERENCES_NAME = "study_abroad_preferences"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        val builder = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "study_abroad.db",
        )
        if (BuildConfig.DEBUG) {
            builder.fallbackToDestructiveMigration(dropAllTables = true)
        }
        return builder.build()
    }

    @Provides
    fun provideScholarshipDao(database: AppDatabase): ScholarshipDao = database.scholarshipDao()

    @Provides
    fun provideCountryDao(database: AppDatabase): CountryDao = database.countryDao()

    @Provides
    fun provideSavedItemDao(database: AppDatabase): SavedItemDao = database.savedItemDao()

    @Provides
    fun provideChecklistProgressDao(database: AppDatabase): ChecklistProgressDao = database.checklistProgressDao()

    @Provides
    fun provideReportDao(database: AppDatabase): ReportDao = database.reportDao()

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES_NAME) },
    )
}
