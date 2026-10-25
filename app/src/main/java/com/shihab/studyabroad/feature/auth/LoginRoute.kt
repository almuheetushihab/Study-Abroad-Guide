package com.shihab.studyabroad.feature.auth

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginRoute(onLoginSuccess: () -> Unit, viewModel: LoginViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val activity = context as? Activity

    DisposableEffect(activity) {
        viewModel.registerActivity(activity)
        onDispose {
            viewModel.registerActivity(null)
        }
    }

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
        onResendOtp = viewModel::onResendOtp,
        onVerifyOtp = viewModel::onVerifyOtp,
        onSignInWithGoogle = viewModel::onSignInWithGoogle,
        onBackToPhone = viewModel::onBackToPhone,
    )
}
