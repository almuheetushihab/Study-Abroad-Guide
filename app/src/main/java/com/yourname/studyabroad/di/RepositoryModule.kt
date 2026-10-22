package com.yourname.studyabroad.di

import com.yourname.studyabroad.data.repository.FakeChecklistRepository
import com.yourname.studyabroad.data.repository.FakeCountryRepository
import com.yourname.studyabroad.data.repository.FakeScholarshipRepository
import com.yourname.studyabroad.data.repository.FakeUniversityRepository
import com.yourname.studyabroad.data.repository.FakeUserProfileRepository
import com.yourname.studyabroad.domain.repository.ChecklistRepository
import com.yourname.studyabroad.domain.repository.CountryRepository
import com.yourname.studyabroad.domain.repository.ScholarshipRepository
import com.yourname.studyabroad.domain.repository.UniversityRepository
import com.yourname.studyabroad.domain.repository.UserProfileRepository
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
