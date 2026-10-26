package com.shihab.studyabroad.feature.auth

import app.cash.turbine.test
import com.shihab.studyabroad.data.repository.FakeAuthRepository
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val userPreferencesRepository: UserPreferencesRepository = mockk(relaxed = true)
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeAuthRepository = FakeAuthRepository(userPreferencesRepository)
        viewModel = LoginViewModel(
            authRepository = fakeAuthRepository,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial step is PHONE_ENTRY`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(LoginStep.PHONE_ENTRY, state.step)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onPhoneNumberChanged updates phone number`() = runTest {
        viewModel.onPhoneNumberChanged("01712345678")

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("01712345678", state.phoneNumber)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSendOtp transitions to OTP_ENTRY on success`() = runTest {
        coEvery { userPreferencesRepository.setSignedIn(true) } returns Unit

        viewModel.onPhoneNumberChanged("01712345678")
        viewModel.onSendOtp()
        testDispatcher.scheduler.advanceUntilIdle()

        val stateAfterSend = viewModel.uiState.value
        assertEquals(LoginStep.OTP_ENTRY, stateAfterSend.step)
    }

    @Test
    fun `onVerifyOtp signs in user on valid 6-digit OTP`() = runTest {
        coEvery { userPreferencesRepository.setSignedIn(true) } returns Unit

        viewModel.onPhoneNumberChanged("01712345678")
        viewModel.onSendOtp()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onOtpCodeChanged("123456")
        viewModel.onVerifyOtp()
        testDispatcher.scheduler.advanceUntilIdle()

        val stateAfterVerify = viewModel.uiState.value
        assertTrue(stateAfterVerify.isSignedIn)
    }

    @Test
    fun `onBackToPhone resets step to PHONE_ENTRY`() = runTest {
        viewModel.onPhoneNumberChanged("01712345678")
        viewModel.onSendOtp()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onBackToPhone()

        val state = viewModel.uiState.value
        assertEquals(LoginStep.PHONE_ENTRY, state.step)
        assertEquals("", state.otpCode)
        assertEquals(0, state.resendCountdown)
    }
}
