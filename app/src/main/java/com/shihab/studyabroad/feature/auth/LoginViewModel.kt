package com.shihab.studyabroad.feature.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.core.common.AuthErrorMapper
import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.repository.AuthRepository
import com.shihab.studyabroad.domain.repository.PhoneAuthLauncher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository, private val phoneAuthLauncher: PhoneAuthLauncher) :
    ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState.Content())
    val uiState: StateFlow<LoginUiState.Content> = _uiState.asStateFlow()

    private var countdownJob: Job? = null

    fun registerActivity(activity: Activity?) {
        phoneAuthLauncher.registerActivity(activity)
    }

    fun onPhoneNumberChanged(number: String) {
        _uiState.value = _uiState.value.copy(
            phoneNumber = number,
            errorMessage = null,
        )
    }

    fun onOtpCodeChanged(code: String) {
        if (code.length <= 6 && code.all { it.isDigit() }) {
            _uiState.value = _uiState.value.copy(
                otpCode = code,
                errorMessage = null,
            )
        }
    }

    fun onSendOtp() {
        val phone = _uiState.value.phoneNumber.trim()
        if (phone.isBlank() || phone.length < 6) {
            _uiState.value = _uiState.value.copy(
                errorMessage = UiText.StringResource(R.string.error_invalid_phone),
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = authRepository.sendOtp(phone)) {
                is AppResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        step = LoginStep.OTP_ENTRY,
                    )
                    startResendTimer()
                }
                is AppResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = AuthErrorMapper.mapExceptionToUiText(result.throwable),
                    )
                }
                else -> {}
            }
        }
    }

    fun onResendOtp() {
        if (_uiState.value.resendCountdown > 0) return

        val phone = _uiState.value.phoneNumber.trim()
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = authRepository.resendOtp(phone)) {
                is AppResult.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    startResendTimer()
                }
                is AppResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = AuthErrorMapper.mapExceptionToUiText(result.throwable),
                    )
                }
                else -> {}
            }
        }
    }

    fun onVerifyOtp() {
        val otp = _uiState.value.otpCode.trim()
        if (otp.length != 6 || !otp.all { it.isDigit() }) {
            _uiState.value = _uiState.value.copy(
                errorMessage = UiText.StringResource(R.string.error_invalid_otp),
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = authRepository.verifyOtp(otp)) {
                is AppResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSignedIn = true,
                    )
                }
                is AppResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = AuthErrorMapper.mapExceptionToUiText(result.throwable),
                    )
                }
                else -> {}
            }
        }
    }

    fun onSignInWithGoogle() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = authRepository.signInWithGoogle()) {
                is AppResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSignedIn = true,
                    )
                }
                is AppResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = AuthErrorMapper.mapExceptionToUiText(result.throwable),
                    )
                }
                else -> {}
            }
        }
    }

    fun onBackToPhone() {
        countdownJob?.cancel()
        _uiState.value = _uiState.value.copy(
            step = LoginStep.PHONE_ENTRY,
            otpCode = "",
            resendCountdown = 0,
            errorMessage = null,
        )
    }

    private fun startResendTimer() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (seconds in 60 downTo 0) {
                _uiState.value = _uiState.value.copy(resendCountdown = seconds)
                if (seconds > 0) delay(1000L)
            }
        }
    }
}
