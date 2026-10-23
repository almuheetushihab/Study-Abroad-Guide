package com.shihab.studyabroad.di

import android.content.Context
import androidx.room.Room
import com.shihab.studyabroad.data.local.AppDatabase
import com.shihab.studyabroad.data.local.dao.ChecklistProgressDao
import com.shihab.studyabroad.data.local.dao.CountryDao
import com.shihab.studyabroad.data.local.dao.ReportDao
import com.shihab.studyabroad.data.local.dao.SavedItemDao
import com.shihab.studyabroad.data.local.dao.ScholarshipDao
import com.shihab.studyabroad.data.local.datastore.UserPreferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "study_abroad.db",
    ).fallbackToDestructiveMigration().build()

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
    fun provideUserPreferencesDataStore(@ApplicationContext context: Context): UserPreferencesDataStore = UserPreferencesDataStore(context)
}
