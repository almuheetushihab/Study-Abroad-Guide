package com.shihab.studyabroad.feature.auth

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shihab.studyabroad.core.common.PhoneAuthLauncher
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface PhoneAuthLauncherEntryPoint {
    fun phoneAuthLauncher(): PhoneAuthLauncher
}

@Composable
fun LoginRoute(onLoginSuccess: () -> Unit, viewModel: LoginViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val activity = context as? Activity
    val phoneAuthLauncher = remember(context) {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            PhoneAuthLauncherEntryPoint::class.java,
        ).phoneAuthLauncher()
    }

    DisposableEffect(activity, phoneAuthLauncher) {
        phoneAuthLauncher.registerActivity(activity)
        onDispose {
            phoneAuthLauncher.registerActivity(null)
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
