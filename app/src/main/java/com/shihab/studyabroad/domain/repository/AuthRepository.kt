package com.shihab.studyabroad.domain.repository

import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.domain.model.AuthUser
import com.shihab.studyabroad.domain.model.DeleteAccountResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<AuthUser?>
    val isSignedIn: Flow<Boolean>

    suspend fun sendOtp(phoneNumber: String): AppResult<Unit>
    suspend fun verifyOtp(otp: String): AppResult<Unit>
    suspend fun resendOtp(phoneNumber: String): AppResult<Unit>
    suspend fun signInWithGoogle(): AppResult<Unit>
    suspend fun signOut()
    suspend fun deleteAccount(): DeleteAccountResult
    suspend fun reauthenticateAndDeleteWithGoogle(): AppResult<Unit>
    suspend fun reauthenticateAndDeleteWithPhone(otp: String): AppResult<Unit>
}
