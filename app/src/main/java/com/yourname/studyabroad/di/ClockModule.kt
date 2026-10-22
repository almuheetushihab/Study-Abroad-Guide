package com.yourname.studyabroad.di

import com.yourname.studyabroad.domain.usecase.GetScholarshipStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClockModule {
    @Provides
    @Singleton
    fun provideClock(): Clock = Clock.System

    @Provides
    @Singleton
    fun provideTimeZone(): TimeZone = TimeZone.currentSystemDefault()

    @Provides
    @Singleton
    fun provideGetScholarshipStatusUseCase(clock: Clock, timeZone: TimeZone): GetScholarshipStatusUseCase =
        GetScholarshipStatusUseCase(clock, timeZone)
}
