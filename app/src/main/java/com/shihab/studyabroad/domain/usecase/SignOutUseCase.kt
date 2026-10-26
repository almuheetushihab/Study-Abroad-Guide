package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.core.common.AnalyticsTracker
import com.shihab.studyabroad.domain.repository.AuthRepository
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val analyticsTracker: AnalyticsTracker,
) {
    suspend operator fun invoke() {
        authRepository.signOut()
        userPreferencesRepository.clearSession()
        analyticsTracker.logEvent("sign_out")
    }
}
