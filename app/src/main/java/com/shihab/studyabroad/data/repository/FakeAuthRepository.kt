package com.shihab.studyabroad.data.repository

import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.domain.model.AuthUser
import com.shihab.studyabroad.domain.repository.AuthRepository
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAuthRepository @Inject constructor(private val userPreferencesRepository: UserPreferencesRepository) : AuthRepository {

    override val currentUser: Flow<AuthUser?> = userPreferencesRepository.isSignedIn.map { signedIn ->
        if (signedIn) {
            AuthUser(
                id = "fake_user_id",
                displayName = "Test Student",
                phone = "+8801712345678",
            )
        } else {
            null
        }
    }

    override val isSignedIn: Flow<Boolean> = userPreferencesRepository.isSignedIn

    override suspend fun sendOtp(phoneNumber: String): AppResult<Unit> {
        if (phoneNumber.isBlank() || phoneNumber.length < 6) {
            return AppResult.Error(IllegalArgumentException("Invalid phone number"))
        }
        return AppResult.Success(Unit)
    }

    override suspend fun verifyOtp(otp: String): AppResult<Unit> {
        if (otp.length != 6 || !otp.all { it.isDigit() }) {
            return AppResult.Error(IllegalArgumentException("Invalid OTP code"))
        }
        userPreferencesRepository.setSignedIn(true)
        return AppResult.Success(Unit)
    }

    override suspend fun resendOtp(phoneNumber: String): AppResult<Unit> = sendOtp(phoneNumber)

    override suspend fun signInWithGoogle(): AppResult<Unit> {
        userPreferencesRepository.setSignedIn(true)
        return AppResult.Success(Unit)
    }

    override suspend fun signOut() {
        userPreferencesRepository.setSignedIn(false)
    }

    override suspend fun deleteAccount(): AppResult<Unit> {
        userPreferencesRepository.setSignedIn(false)
        return AppResult.Success(Unit)
    }
}
