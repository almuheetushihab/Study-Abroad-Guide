package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.domain.model.Track
import com.shihab.studyabroad.domain.model.UserProfile
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import com.shihab.studyabroad.navigation.Route
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetStartDestinationUseCaseTest {

    private val userPreferencesRepository: UserPreferencesRepository = mockk()
    private lateinit var useCase: GetStartDestinationUseCase

    @Before
    fun setUp() {
        useCase = GetStartDestinationUseCase(userPreferencesRepository)
    }

    @Test
    fun `returns Onboarding when onboarding is not completed`() = runTest {
        every { userPreferencesRepository.isOnboardingDone } returns flowOf(false)
        every { userPreferencesRepository.isSignedIn } returns flowOf(false)
        every { userPreferencesRepository.selectedTrack } returns flowOf(null)
        every { userPreferencesRepository.userProfile } returns flowOf(null)

        val destination = useCase().first()
        assertEquals(Route.Onboarding, destination)
    }

    @Test
    fun `returns Auth when onboarded but not signed in`() = runTest {
        every { userPreferencesRepository.isOnboardingDone } returns flowOf(true)
        every { userPreferencesRepository.isSignedIn } returns flowOf(false)
        every { userPreferencesRepository.selectedTrack } returns flowOf(null)
        every { userPreferencesRepository.userProfile } returns flowOf(null)

        val destination = useCase().first()
        assertEquals(Route.Auth, destination)
    }

    @Test
    fun `returns TrackSelect when signed in but no track selected`() = runTest {
        every { userPreferencesRepository.isOnboardingDone } returns flowOf(true)
        every { userPreferencesRepository.isSignedIn } returns flowOf(true)
        every { userPreferencesRepository.selectedTrack } returns flowOf(null)
        every { userPreferencesRepository.userProfile } returns flowOf(null)

        val destination = useCase().first()
        assertEquals(Route.TrackSelect, destination)
    }

    @Test
    fun `returns ProfileSetup when signed in and track selected but profile incomplete`() = runTest {
        every { userPreferencesRepository.isOnboardingDone } returns flowOf(true)
        every { userPreferencesRepository.isSignedIn } returns flowOf(true)
        every { userPreferencesRepository.selectedTrack } returns flowOf(Track.GRADUATE)
        every { userPreferencesRepository.userProfile } returns flowOf(null)

        val destination = useCase().first()
        assertEquals(Route.ProfileSetup(isEditMode = false), destination)
    }

    @Test
    fun `returns Home when onboarded, signed in, track selected, and profile complete`() = runTest {
        val completeProfile = UserProfile(level = "Master", subject = "Computer Science")
        every { userPreferencesRepository.isOnboardingDone } returns flowOf(true)
        every { userPreferencesRepository.isSignedIn } returns flowOf(true)
        every { userPreferencesRepository.selectedTrack } returns flowOf(Track.GRADUATE)
        every { userPreferencesRepository.userProfile } returns flowOf(completeProfile)

        val destination = useCase().first()
        assertEquals(Route.Home, destination)
    }
}
