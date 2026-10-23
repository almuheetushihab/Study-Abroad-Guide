package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import com.shihab.studyabroad.navigation.Route
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetStartDestinationUseCase @Inject constructor(private val userPreferencesRepository: UserPreferencesRepository) {
    operator fun invoke(): Flow<Route> = combine(
        userPreferencesRepository.isOnboardingDone,
        userPreferencesRepository.isSignedIn,
        userPreferencesRepository.selectedTrack,
        userPreferencesRepository.userProfile,
    ) { onboardingDone, signedIn, track, profile ->
        when {
            !onboardingDone -> Route.Onboarding
            !signedIn -> Route.Auth
            track == null -> Route.TrackSelect
            profile == null || profile.level.isBlank() -> Route.ProfileSetup(isEditMode = false)
            else -> Route.Home
        }
    }
}
