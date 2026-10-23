package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.data.local.AppDatabase
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val appDatabase: AppDatabase,
) {
    suspend operator fun invoke(): AppResult<Unit> = try {
        appDatabase.clearAllTables()
        userPreferencesRepository.clearSession()
        AppResult.Success(Unit)
    } catch (e: Exception) {
        AppResult.Error(e)
    }
}
