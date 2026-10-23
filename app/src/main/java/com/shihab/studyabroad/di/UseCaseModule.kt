package com.shihab.studyabroad.di

import com.shihab.studyabroad.domain.usecase.CompareRequirementUseCase
import com.shihab.studyabroad.domain.usecase.IsRecordStaleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideCompareRequirementUseCase(): CompareRequirementUseCase = CompareRequirementUseCase()

    @Provides
    @Singleton
    fun provideIsRecordStaleUseCase(clock: Clock, timeZone: TimeZone): IsRecordStaleUseCase = IsRecordStaleUseCase(clock, timeZone)
}
