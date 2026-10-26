package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.core.common.AnalyticsTracker
import com.shihab.studyabroad.domain.repository.AuthRepository
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SignOutUseCaseTest {

    private val authRepository: AuthRepository = mockk(relaxed = true)
    private val userPreferencesRepository: UserPreferencesRepository = mockk(relaxed = true)
    private val analyticsTracker: AnalyticsTracker = mockk(relaxed = true)
    private lateinit var signOutUseCase: SignOutUseCase

    @Before
    fun setUp() {
        signOutUseCase = SignOutUseCase(
            authRepository = authRepository,
            userPreferencesRepository = userPreferencesRepository,
            analyticsTracker = analyticsTracker,
        )
    }

    @Test
    fun `sign out calls authRepository signOut, clears preferences session, and logs analytics event`() = runTest {
        signOutUseCase()

        coVerify(exactly = 1) { authRepository.signOut() }
        coVerify(exactly = 1) { userPreferencesRepository.clearSession() }
        coVerify(exactly = 1) { analyticsTracker.logEvent("sign_out") }
    }
}
