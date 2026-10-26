package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.domain.model.AuthProvider
import com.shihab.studyabroad.domain.model.DeleteAccountResult
import com.shihab.studyabroad.domain.repository.AuthRepository
import com.shihab.studyabroad.domain.repository.LocalDataCleaner
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteAccountUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var localDataCleaner: LocalDataCleaner
    private lateinit var deleteAccountUseCase: DeleteAccountUseCase

    @Before
    fun setUp() {
        authRepository = mockk(relaxed = true)
        localDataCleaner = mockk(relaxed = true)
        coEvery { localDataCleaner.clearAllLocalData() } returns AppResult.Success(Unit)
        deleteAccountUseCase = DeleteAccountUseCase(
            authRepository = authRepository,
            localDataCleaner = localDataCleaner,
        )
    }

    @Test
    fun `initialDelete succeeds directly and clears all local data`() = runTest {
        coEvery { authRepository.deleteAccount() } returns DeleteAccountResult.Success

        val result = deleteAccountUseCase.initialDelete()

        assertEquals(DeleteAccountResult.Success, result)
        coVerify(exactly = 1) { authRepository.deleteAccount() }
        coVerify(exactly = 1) { localDataCleaner.clearAllLocalData() }
    }

    @Test
    fun `initialDelete returns ReauthenticationRequired and does NOT clear local data`() = runTest {
        coEvery { authRepository.deleteAccount() } returns DeleteAccountResult.ReauthenticationRequired(AuthProvider.PHONE)

        val result = deleteAccountUseCase.initialDelete()

        assertEquals(DeleteAccountResult.ReauthenticationRequired(AuthProvider.PHONE), result)
        coVerify(exactly = 1) { authRepository.deleteAccount() }
        coVerify(exactly = 0) { localDataCleaner.clearAllLocalData() }
    }

    @Test
    fun `initialDelete fails with error and does NOT clear local data`() = runTest {
        val error = Exception("Delete failed")
        coEvery { authRepository.deleteAccount() } returns DeleteAccountResult.Error(error)

        val result = deleteAccountUseCase.initialDelete()

        assertEquals(DeleteAccountResult.Error(error), result)
        coVerify(exactly = 1) { authRepository.deleteAccount() }
        coVerify(exactly = 0) { localDataCleaner.clearAllLocalData() }
    }

    @Test
    fun `initialDelete handles cleaner failure and returns error`() = runTest {
        val cleanError = Exception("Failed to clear Room database")
        coEvery { authRepository.deleteAccount() } returns DeleteAccountResult.Success
        coEvery { localDataCleaner.clearAllLocalData() } returns AppResult.Error(cleanError)

        val result = deleteAccountUseCase.initialDelete()

        assertEquals(DeleteAccountResult.Error(cleanError), result)
        coVerify(exactly = 1) { localDataCleaner.clearAllLocalData() }
    }

    @Test
    fun `reauthenticateAndDeleteWithGoogle succeeds and clears all local data`() = runTest {
        coEvery { authRepository.reauthenticateAndDeleteWithGoogle() } returns AppResult.Success(Unit)

        val result = deleteAccountUseCase.reauthenticateAndDeleteWithGoogle()

        assertTrue(result is AppResult.Success)
        coVerify(exactly = 1) { authRepository.reauthenticateAndDeleteWithGoogle() }
        coVerify(exactly = 1) { localDataCleaner.clearAllLocalData() }
    }

    @Test
    fun `reauthenticateAndDeleteWithGoogle fails and keeps all local data`() = runTest {
        val error = Exception("Google re-auth failed")
        coEvery { authRepository.reauthenticateAndDeleteWithGoogle() } returns AppResult.Error(error)

        val result = deleteAccountUseCase.reauthenticateAndDeleteWithGoogle()

        assertTrue(result is AppResult.Error)
        coVerify(exactly = 1) { authRepository.reauthenticateAndDeleteWithGoogle() }
        coVerify(exactly = 0) { localDataCleaner.clearAllLocalData() }
    }

    @Test
    fun `reauthenticateAndDeleteWithPhone succeeds and clears all local data`() = runTest {
        coEvery { authRepository.reauthenticateAndDeleteWithPhone("123456") } returns AppResult.Success(Unit)

        val result = deleteAccountUseCase.reauthenticateAndDeleteWithPhone("123456")

        assertTrue(result is AppResult.Success)
        coVerify(exactly = 1) { authRepository.reauthenticateAndDeleteWithPhone("123456") }
        coVerify(exactly = 1) { localDataCleaner.clearAllLocalData() }
    }

    @Test
    fun `reauthenticateAndDeleteWithPhone fails and keeps all local data`() = runTest {
        val error = Exception("Phone re-auth failed")
        coEvery { authRepository.reauthenticateAndDeleteWithPhone("123456") } returns AppResult.Error(error)

        val result = deleteAccountUseCase.reauthenticateAndDeleteWithPhone("123456")

        assertTrue(result is AppResult.Error)
        coVerify(exactly = 1) { authRepository.reauthenticateAndDeleteWithPhone("123456") }
        coVerify(exactly = 0) { localDataCleaner.clearAllLocalData() }
    }
}
