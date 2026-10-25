package com.shihab.studyabroad.di

import com.shihab.studyabroad.core.common.AnalyticsTracker
import com.shihab.studyabroad.core.common.AndroidUrlOpener
import com.shihab.studyabroad.core.common.CrashReporter
import com.shihab.studyabroad.core.common.UrlOpener
import com.shihab.studyabroad.data.local.datastore.UserPreferencesRepositoryImpl
import com.shihab.studyabroad.data.repository.AndroidPhoneAuthLauncher
import com.shihab.studyabroad.data.repository.ChecklistRepositoryImpl
import com.shihab.studyabroad.data.repository.FakeCountryRepository
import com.shihab.studyabroad.data.repository.FakeScholarshipRepository
import com.shihab.studyabroad.data.repository.FakeUniversityRepository
import com.shihab.studyabroad.data.repository.FakeUserProfileRepository
import com.shihab.studyabroad.data.repository.FirebaseAnalyticsTracker
import com.shihab.studyabroad.data.repository.FirebaseAuthRepository
import com.shihab.studyabroad.data.repository.FirebaseCrashReporter
import com.shihab.studyabroad.data.repository.ReportRepositoryImpl
import com.shihab.studyabroad.data.repository.SavedRepositoryImpl
import com.shihab.studyabroad.domain.repository.AuthRepository
import com.shihab.studyabroad.domain.repository.ChecklistRepository
import com.shihab.studyabroad.domain.repository.CountryRepository
import com.shihab.studyabroad.domain.repository.PhoneAuthLauncher
import com.shihab.studyabroad.domain.repository.ReportRepository
import com.shihab.studyabroad.domain.repository.SavedRepository
import com.shihab.studyabroad.domain.repository.ScholarshipRepository
import com.shihab.studyabroad.domain.repository.UniversityRepository
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import com.shihab.studyabroad.domain.repository.UserProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: FirebaseAuthRepository): AuthRepository

    @Binds
    @Singleton
    abstract fun bindPhoneAuthLauncher(impl: AndroidPhoneAuthLauncher): PhoneAuthLauncher

    @Binds
    @Singleton
    abstract fun bindCrashReporter(impl: FirebaseCrashReporter): CrashReporter

    @Binds
    @Singleton
    abstract fun bindAnalyticsTracker(impl: FirebaseAnalyticsTracker): AnalyticsTracker

    @Binds
    @Singleton
    abstract fun bindScholarshipRepository(fakeScholarshipRepository: FakeScholarshipRepository): ScholarshipRepository

    @Binds
    @Singleton
    abstract fun bindCountryRepository(fakeCountryRepository: FakeCountryRepository): CountryRepository

    @Binds
    @Singleton
    abstract fun bindUniversityRepository(fakeUniversityRepository: FakeUniversityRepository): UniversityRepository

    @Binds
    @Singleton
    abstract fun bindUserProfileRepository(fakeUserProfileRepository: FakeUserProfileRepository): UserProfileRepository

    @Binds
    @Singleton
    abstract fun bindChecklistRepository(checklistRepositoryImpl: ChecklistRepositoryImpl): ChecklistRepository

    @Binds
    @Singleton
    abstract fun bindSavedRepository(savedRepositoryImpl: SavedRepositoryImpl): SavedRepository

    @Binds
    @Singleton
    abstract fun bindReportRepository(reportRepositoryImpl: ReportRepositoryImpl): ReportRepository

    @Binds
    @Singleton
    abstract fun bindUrlOpener(androidUrlOpener: AndroidUrlOpener): UrlOpener
}
