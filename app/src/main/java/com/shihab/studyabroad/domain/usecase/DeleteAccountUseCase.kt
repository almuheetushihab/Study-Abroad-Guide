package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.domain.model.DeleteAccountResult
import com.shihab.studyabroad.domain.repository.AuthRepository
import com.shihab.studyabroad.domain.repository.LocalDataCleaner
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val localDataCleaner: LocalDataCleaner,
) {
    suspend fun initialDelete(): DeleteAccountResult {
        val result = authRepository.deleteAccount()
        if (result is DeleteAccountResult.Success) {
            val cleanResult = localDataCleaner.clearAllLocalData()
            if (cleanResult is AppResult.Error) {
                return DeleteAccountResult.Error(cleanResult.throwable)
            }
        }
        return result
    }

    suspend fun reauthenticateAndDeleteWithGoogle(): AppResult<Unit> {
        val result = authRepository.reauthenticateAndDeleteWithGoogle()
        if (result is AppResult.Success) {
            return localDataCleaner.clearAllLocalData()
        }
        return result
    }

    suspend fun reauthenticateAndDeleteWithPhone(otp: String): AppResult<Unit> {
        val result = authRepository.reauthenticateAndDeleteWithPhone(otp)
        if (result is AppResult.Success) {
            return localDataCleaner.clearAllLocalData()
        }
        return result
    }

    suspend fun sendReauthOtp(phoneNumber: String): AppResult<Unit> = authRepository.resendOtp(phoneNumber)
}
