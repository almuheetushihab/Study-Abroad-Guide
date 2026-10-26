package com.shihab.studyabroad.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.BuildConfig
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.core.common.AuthErrorMapper
import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.AuthProvider
import com.shihab.studyabroad.domain.model.AuthUser
import com.shihab.studyabroad.domain.model.DeleteAccountResult
import com.shihab.studyabroad.domain.model.UserProfile
import com.shihab.studyabroad.domain.repository.AuthRepository
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import com.shihab.studyabroad.domain.usecase.DeleteAccountUseCase
import com.shihab.studyabroad.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository,
    private val authRepository: AuthRepository,
    private val signOutUseCase: SignOutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
) : ViewModel() {

    private val isProcessing = MutableStateFlow(false)
    private val showSignOutDialog = MutableStateFlow(false)
    private val showDeleteDialog = MutableStateFlow(false)
    private val showReauthDialog = MutableStateFlow(false)
    private val reauthProvider = MutableStateFlow<AuthProvider?>(null)
    private val showOtpDialog = MutableStateFlow(false)
    private val otpCode = MutableStateFlow("")
    private val otpResendSeconds = MutableStateFlow(0)
    private val errorMessage = MutableStateFlow<UiText?>(null)

    private var countdownJob: Job? = null

    val uiState: StateFlow<ProfileUiState> = combine(
        userPreferencesRepository.userProfile,
        authRepository.currentUser,
        isProcessing,
        showSignOutDialog,
        showDeleteDialog,
        showReauthDialog,
        reauthProvider,
        showOtpDialog,
        otpCode,
        otpResendSeconds,
        errorMessage,
    ) { args ->
        val profile = args[0] as? UserProfile
        val currentUser = args[1] as? AuthUser
        val processing = args[2] as Boolean
        val signOutDlg = args[3] as Boolean
        val deleteDlg = args[4] as Boolean
        val reauthDlg = args[5] as Boolean
        val provider = args[6] as? AuthProvider ?: currentUser?.provider
        val otpDlg = args[7] as Boolean
        val code = args[8] as String
        val resendSecs = args[9] as Int
        val err = args[10] as? UiText

        ProfileUiState.Content(
            userProfile = profile,
            appVersion = BuildConfig.VERSION_NAME,
            userPhone = currentUser?.phone ?: "",
            isProcessing = processing,
            showSignOutDialog = signOutDlg,
            showDeleteDialog = deleteDlg,
            showReauthDialog = reauthDlg,
            reauthProvider = provider,
            showOtpDialog = otpDlg,
            otpCode = code,
            otpResendSeconds = resendSecs,
            errorMessage = err,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState.Loading,
    )

    fun onSignOutClicked() {
        errorMessage.value = null
        showSignOutDialog.value = true
    }

    fun onDismissSignOutDialog() {
        showSignOutDialog.value = false
    }

    fun onConfirmSignOut(onSignedOut: () -> Unit) {
        viewModelScope.launch {
            if (isProcessing.value) return@launch
            isProcessing.value = true
            showSignOutDialog.value = false
            try {
                signOutUseCase()
                onSignedOut()
            } catch (e: Exception) {
                errorMessage.value = AuthErrorMapper.mapExceptionToUiText(e)
            } finally {
                isProcessing.value = false
            }
        }
    }

    fun onDeleteAccountClicked() {
        errorMessage.value = null
        showDeleteDialog.value = true
    }

    fun onDismissDeleteDialog() {
        showDeleteDialog.value = false
    }

    fun onConfirmDeleteAccount(onDeletedToOnboarding: () -> Unit) {
        viewModelScope.launch {
            if (isProcessing.value) return@launch
            isProcessing.value = true
            showDeleteDialog.value = false
            when (val result = deleteAccountUseCase.initialDelete()) {
                is DeleteAccountResult.Success -> {
                    isProcessing.value = false
                    onDeletedToOnboarding()
                }
                is DeleteAccountResult.ReauthenticationRequired -> {
                    isProcessing.value = false
                    reauthProvider.value = result.provider
                    showReauthDialog.value = true
                }
                is DeleteAccountResult.Error -> {
                    isProcessing.value = false
                    errorMessage.value = AuthErrorMapper.mapExceptionToUiText(result.throwable)
                }
            }
        }
    }

    fun onDismissReauthDialog() {
        showReauthDialog.value = false
        errorMessage.value = UiText.StringResource(R.string.me_reauth_cancelled)
    }

    fun onConfirmReauth(onDeletedToOnboarding: () -> Unit) {
        val provider = reauthProvider.value ?: AuthProvider.PHONE
        showReauthDialog.value = false

        when (provider) {
            AuthProvider.GOOGLE -> {
                viewModelScope.launch {
                    if (isProcessing.value) return@launch
                    isProcessing.value = true
                    when (val result = deleteAccountUseCase.reauthenticateAndDeleteWithGoogle()) {
                        is AppResult.Success -> {
                            isProcessing.value = false
                            onDeletedToOnboarding()
                        }
                        is AppResult.Error -> {
                            isProcessing.value = false
                            errorMessage.value = AuthErrorMapper.mapExceptionToUiText(result.throwable)
                        }
                        else -> {
                            isProcessing.value = false
                        }
                    }
                }
            }
            AuthProvider.PHONE, AuthProvider.UNKNOWN -> {
                viewModelScope.launch {
                    val user = authRepository.currentUser.firstOrNull()
                    val phone = user?.phone ?: ""
                    sendOtpForReauth(phone)
                }
            }
        }
    }

    private fun sendOtpForReauth(phone: String) {
        viewModelScope.launch {
            if (isProcessing.value) return@launch
            isProcessing.value = true
            when (val result = deleteAccountUseCase.sendReauthOtp(phone)) {
                is AppResult.Success -> {
                    isProcessing.value = false
                    showOtpDialog.value = true
                    startResendCountdown()
                }
                is AppResult.Error -> {
                    isProcessing.value = false
                    errorMessage.value = AuthErrorMapper.mapExceptionToUiText(result.throwable)
                }
                else -> {
                    isProcessing.value = false
                }
            }
        }
    }

    fun onOtpCodeChanged(code: String) {
        if (code.length <= 6 && code.all { it.isDigit() }) {
            otpCode.value = code
        }
    }

    fun onResendOtpClicked() {
        if (otpResendSeconds.value > 0 || isProcessing.value) return
        viewModelScope.launch {
            val user = authRepository.currentUser.firstOrNull()
            val phone = user?.phone ?: ""
            sendOtpForReauth(phone)
        }
    }

    fun onConfirmOtpAndDelete(onDeletedToOnboarding: () -> Unit) {
        val code = otpCode.value
        if (code.length != 6) return
        viewModelScope.launch {
            if (isProcessing.value) return@launch
            isProcessing.value = true
            when (val result = deleteAccountUseCase.reauthenticateAndDeleteWithPhone(code)) {
                is AppResult.Success -> {
                    isProcessing.value = false
                    showOtpDialog.value = false
                    onDeletedToOnboarding()
                }
                is AppResult.Error -> {
                    isProcessing.value = false
                    errorMessage.value = AuthErrorMapper.mapExceptionToUiText(result.throwable)
                }
                else -> {
                    isProcessing.value = false
                }
            }
        }
    }

    fun onDismissOtpDialog() {
        showOtpDialog.value = false
        otpCode.value = ""
        errorMessage.value = UiText.StringResource(R.string.me_reauth_cancelled)
    }

    fun clearErrorMessage() {
        errorMessage.value = null
    }

    private fun startResendCountdown() {
        countdownJob?.cancel()
        otpResendSeconds.value = 60
        countdownJob = viewModelScope.launch {
            while (otpResendSeconds.value > 0) {
                delay(1000L)
                otpResendSeconds.value -= 1
            }
        }
    }
}
