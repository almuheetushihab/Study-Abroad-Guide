package com.shihab.studyabroad.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.core.designsystem.component.AppErrorContent
import com.shihab.studyabroad.core.designsystem.component.AppLoadingWheel
import com.shihab.studyabroad.core.designsystem.component.ScamAlertCard
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.feature.auth.components.OtpVerifyForm
import com.shihab.studyabroad.feature.auth.components.PhoneLoginForm

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onPhoneNumberChanged: (String) -> Unit,
    onOtpCodeChanged: (String) -> Unit,
    onSendOtp: () -> Unit,
    onResendOtp: () -> Unit,
    onVerifyOtp: () -> Unit,
    onSignInWithGoogle: () -> Unit,
    onBackToPhone: () -> Unit,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
) {
    when (uiState) {
        is LoginUiState.Loading -> AppLoadingWheel(modifier = modifier)
        is LoginUiState.Error -> AppErrorContent(
            message = uiState.message.asString(),
            onRetry = onRetry,
            modifier = modifier,
        )
        is LoginUiState.Content -> {
            LoginContent(
                uiState = uiState,
                onPhoneNumberChanged = onPhoneNumberChanged,
                onOtpCodeChanged = onOtpCodeChanged,
                onSendOtp = onSendOtp,
                onResendOtp = onResendOtp,
                onVerifyOtp = onVerifyOtp,
                onSignInWithGoogle = onSignInWithGoogle,
                onBackToPhone = onBackToPhone,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun LoginContent(
    uiState: LoginUiState.Content,
    onPhoneNumberChanged: (String) -> Unit,
    onOtpCodeChanged: (String) -> Unit,
    onSendOtp: () -> Unit,
    onResendOtp: () -> Unit,
    onVerifyOtp: () -> Unit,
    onSignInWithGoogle: () -> Unit,
    onBackToPhone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Spacer(modifier = Modifier.height(32.dp))
            when (uiState.step) {
                LoginStep.PHONE_ENTRY -> {
                    PhoneLoginForm(
                        phoneNumber = uiState.phoneNumber,
                        onPhoneNumberChanged = onPhoneNumberChanged,
                        onSendOtp = onSendOtp,
                        onSignInWithGoogle = onSignInWithGoogle,
                        isLoading = uiState.isLoading,
                        errorMessage = uiState.errorMessage,
                    )
                }
                LoginStep.OTP_ENTRY -> {
                    OtpVerifyForm(
                        phoneNumber = uiState.phoneNumber,
                        otpCode = uiState.otpCode,
                        onOtpCodeChanged = onOtpCodeChanged,
                        onVerifyOtp = onVerifyOtp,
                        onResendOtp = onResendOtp,
                        onBackToPhone = onBackToPhone,
                        isLoading = uiState.isLoading,
                        resendCountdown = uiState.resendCountdown,
                        errorMessage = uiState.errorMessage,
                    )
                }
            }
        }

        ScamAlertCard()
    }
}

@ThemePreviews
@Composable
fun LoginScreenPhonePreview() {
    StudyAbroadTheme {
        LoginScreen(
            uiState = LoginUiState.Content(step = LoginStep.PHONE_ENTRY),
            onPhoneNumberChanged = {},
            onOtpCodeChanged = {},
            onSendOtp = {},
            onResendOtp = {},
            onVerifyOtp = {},
            onSignInWithGoogle = {},
            onBackToPhone = {},
        )
    }
}

@ThemePreviews
@Composable
fun LoginScreenOtpPreview() {
    StudyAbroadTheme {
        LoginScreen(
            uiState = LoginUiState.Content(
                step = LoginStep.OTP_ENTRY,
                phoneNumber = "01712345678",
                otpCode = "123",
                errorMessage = UiText.DynamicString("Invalid OTP code. Please enter 6 digits."),
            ),
            onPhoneNumberChanged = {},
            onOtpCodeChanged = {},
            onSendOtp = {},
            onResendOtp = {},
            onVerifyOtp = {},
            onSignInWithGoogle = {},
            onBackToPhone = {},
        )
    }
}
