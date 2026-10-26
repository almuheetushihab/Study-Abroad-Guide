package com.shihab.studyabroad.feature.profile

import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.AuthProvider
import com.shihab.studyabroad.domain.model.AuthUser
import com.shihab.studyabroad.domain.model.DeleteAccountResult
import com.shihab.studyabroad.domain.model.UserProfile
import com.shihab.studyabroad.domain.repository.AuthRepository
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import com.shihab.studyabroad.domain.usecase.DeleteAccountUseCase
import com.shihab.studyabroad.domain.usecase.SignOutUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val userPreferencesRepository: UserPreferencesRepository = mockk(relaxed = true)
    private val authRepository: AuthRepository = mockk(relaxed = true)
    private val signOutUseCase: SignOutUseCase = mockk(relaxed = true)
    private val deleteAccountUseCase: DeleteAccountUseCase = mockk(relaxed = true)

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        everyDefaults()
        viewModel = ProfileViewModel(
            userPreferencesRepository = userPreferencesRepository,
            authRepository = authRepository,
            signOutUseCase = signOutUseCase,
            deleteAccountUseCase = deleteAccountUseCase,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun everyDefaults() {
        coEvery { userPreferencesRepository.userProfile } returns flowOf(UserProfile(level = "Master", subject = "CS"))
        coEvery { authRepository.currentUser } returns flowOf(AuthUser(id = "123", phone = "+8801712345678", provider = AuthProvider.PHONE))
    }

    private suspend fun getContentState(): ProfileUiState.Content = viewModel.uiState.first {
        it is ProfileUiState.Content
    } as ProfileUiState.Content

    @Test
    fun `onSignOutClicked sets showSignOutDialog to true`() = runTest {
        viewModel.onSignOutClicked()
        advanceUntilIdle()

        val state = getContentState()
        assertTrue(state.showSignOutDialog)
    }

    @Test
    fun `onConfirmSignOut executes signOutUseCase and triggers callback`() = runTest {
        var signedOut = false
        viewModel.onSignOutClicked()
        viewModel.onConfirmSignOut { signedOut = true }
        advanceUntilIdle()

        coVerify(exactly = 1) { signOutUseCase() }
        assertTrue(signedOut)
    }

    @Test
    fun `onDeleteAccountClicked sets showDeleteDialog to true`() = runTest {
        viewModel.onDeleteAccountClicked()
        advanceUntilIdle()

        val state = getContentState()
        assertTrue(state.showDeleteDialog)
    }

    @Test
    fun `onConfirmDeleteAccount when direct success invokes callback`() = runTest {
        coEvery { deleteAccountUseCase.initialDelete() } returns DeleteAccountResult.Success
        var deleted = false

        viewModel.onConfirmDeleteAccount { deleted = true }
        advanceUntilIdle()

        coVerify(exactly = 1) { deleteAccountUseCase.initialDelete() }
        assertTrue(deleted)
    }

    @Test
    fun `onConfirmDeleteAccount when reauth required shows reauth dialog`() = runTest {
        coEvery { deleteAccountUseCase.initialDelete() } returns DeleteAccountResult.ReauthenticationRequired(AuthProvider.GOOGLE)

        viewModel.onConfirmDeleteAccount { }
        advanceUntilIdle()

        val state = getContentState()
        assertFalse(state.showDeleteDialog)
        assertTrue(state.showReauthDialog)
        assertEquals(AuthProvider.GOOGLE, state.reauthProvider)
    }

    @Test
    fun `onConfirmReauth for Google calls Google re-auth and invokes callback on success`() = runTest {
        coEvery { deleteAccountUseCase.initialDelete() } returns DeleteAccountResult.ReauthenticationRequired(AuthProvider.GOOGLE)
        coEvery { deleteAccountUseCase.reauthenticateAndDeleteWithGoogle() } returns AppResult.Success(Unit)
        var deleted = false

        viewModel.onConfirmDeleteAccount { }
        advanceUntilIdle() // sets reauth required

        viewModel.onConfirmReauth { deleted = true }
        advanceUntilIdle()

        coVerify(exactly = 1) { deleteAccountUseCase.reauthenticateAndDeleteWithGoogle() }
        assertTrue(deleted)
    }

    @Test
    fun `onConfirmReauth for Phone sends OTP and shows OTP dialog`() = runTest {
        coEvery { deleteAccountUseCase.initialDelete() } returns DeleteAccountResult.ReauthenticationRequired(AuthProvider.PHONE)
        coEvery { deleteAccountUseCase.sendReauthOtp(any()) } returns AppResult.Success(Unit)

        viewModel.onConfirmDeleteAccount { }
        advanceUntilIdle()

        viewModel.onConfirmReauth { }
        advanceUntilIdle()

        val state = getContentState()
        assertTrue(state.showOtpDialog)
        coVerify(exactly = 1) { deleteAccountUseCase.sendReauthOtp("+8801712345678") }
    }

    @Test
    fun `onConfirmOtpAndDelete calls phone reauth and invokes callback on success`() = runTest {
        coEvery { deleteAccountUseCase.reauthenticateAndDeleteWithPhone("123456") } returns AppResult.Success(Unit)
        var deleted = false

        viewModel.onOtpCodeChanged("123456")
        viewModel.onConfirmOtpAndDelete { deleted = true }
        advanceUntilIdle()

        coVerify(exactly = 1) { deleteAccountUseCase.reauthenticateAndDeleteWithPhone("123456") }
        assertTrue(deleted)
    }

    @Test
    fun `onDismissReauthDialog cancels re-auth and sets cancellation error message`() = runTest {
        viewModel.onDismissReauthDialog()
        advanceUntilIdle()

        val state = getContentState()
        assertFalse(state.showReauthDialog)
        assertEquals(UiText.StringResource(R.string.me_reauth_cancelled), state.errorMessage)
    }
}
