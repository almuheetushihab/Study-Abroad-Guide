package com.shihab.studyabroad.di

import com.shihab.studyabroad.data.local.datastore.UserPreferencesRepositoryImpl
import com.shihab.studyabroad.data.repository.FakeAuthRepository
import com.shihab.studyabroad.data.repository.FakeChecklistRepository
import com.shihab.studyabroad.data.repository.FakeCountryRepository
import com.shihab.studyabroad.data.repository.FakeScholarshipRepository
import com.shihab.studyabroad.data.repository.FakeUniversityRepository
import com.shihab.studyabroad.data.repository.FakeUserProfileRepository
import com.shihab.studyabroad.domain.repository.AuthRepository
import com.shihab.studyabroad.domain.repository.ChecklistRepository
import com.shihab.studyabroad.domain.repository.CountryRepository
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
    abstract fun bindAuthRepository(impl: FakeAuthRepository): AuthRepository

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
    abstract fun bindChecklistRepository(fakeChecklistRepository: FakeChecklistRepository): ChecklistRepository
}
