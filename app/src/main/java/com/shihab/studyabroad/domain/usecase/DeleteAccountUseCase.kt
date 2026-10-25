package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.data.local.AppDatabase
import com.shihab.studyabroad.domain.repository.AuthRepository
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val appDatabase: AppDatabase,
) {
    suspend operator fun invoke(): AppResult<Unit> {
        val authResult = authRepository.deleteAccount()
        if (authResult is AppResult.Error) {
            return authResult
        }
        return try {
            appDatabase.clearAllTables()
            userPreferencesRepository.clearSession()
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(e)
        }
    }
}
