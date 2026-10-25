package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.data.local.AppDatabase
import com.shihab.studyabroad.domain.repository.AuthRepository
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteAccountUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var appDatabase: AppDatabase
    private lateinit var deleteAccountUseCase: DeleteAccountUseCase

    @Before
    fun setUp() {
        authRepository = mockk(relaxed = true)
        userPreferencesRepository = mockk(relaxed = true)
        appDatabase = mockk(relaxed = true)
        deleteAccountUseCase = DeleteAccountUseCase(
            authRepository = authRepository,
            userPreferencesRepository = userPreferencesRepository,
            appDatabase = appDatabase,
        )
    }

    @Test
    fun `invoke clears database tables and clears session in preferences`() = runTest {
        coEvery { authRepository.deleteAccount() } returns AppResult.Success(Unit)
        coEvery { userPreferencesRepository.clearSession() } returns Unit

        val result = deleteAccountUseCase()

        assertTrue(result is AppResult.Success)
        coVerify(exactly = 1) { authRepository.deleteAccount() }
        coVerify(exactly = 1) { appDatabase.clearAllTables() }
        coVerify(exactly = 1) { userPreferencesRepository.clearSession() }
    }
}
