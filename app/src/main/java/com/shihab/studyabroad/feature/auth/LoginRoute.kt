package com.shihab.studyabroad.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginRoute(onLoginSuccess: () -> Unit, viewModel: LoginViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSignedIn) {
        if (uiState.isSignedIn) {
            onLoginSuccess()
        }
    }

    LoginScreen(
        uiState = uiState,
        onPhoneNumberChanged = viewModel::onPhoneNumberChanged,
        onOtpCodeChanged = viewModel::onOtpCodeChanged,
        onSendOtp = viewModel::onSendOtp,
        onVerifyOtp = viewModel::onVerifyOtp,
        onSignInWithGoogle = viewModel::onSignInWithGoogle,
        onBackToPhone = viewModel::onBackToPhone,
    )
}
