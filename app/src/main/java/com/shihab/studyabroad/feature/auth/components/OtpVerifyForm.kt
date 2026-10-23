package com.shihab.studyabroad.feature.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.designsystem.component.AppTextField
import com.shihab.studyabroad.core.designsystem.component.PrimaryButton
import com.shihab.studyabroad.core.designsystem.component.SecondaryButton
import com.shihab.studyabroad.core.ui.UiText

@Composable
fun OtpVerifyForm(
    phoneNumber: String,
    otpCode: String,
    onOtpCodeChanged: (String) -> Unit,
    onVerifyOtp: () -> Unit,
    onResendOtp: () -> Unit,
    onBackToPhone: () -> Unit,
    isLoading: Boolean,
    errorMessage: UiText?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.login_otp_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.login_otp_subtitle, phoneNumber),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppTextField(
            value = otpCode,
            onValueChange = onOtpCodeChanged,
            label = stringResource(id = R.string.login_otp_label),
            isError = errorMessage != null,
            supportingText = errorMessage?.asString(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = stringResource(id = R.string.btn_verify_otp),
            onClick = onVerifyOtp,
            enabled = !isLoading && otpCode.length == 6,
        )

        Spacer(modifier = Modifier.height(12.dp))

        SecondaryButton(
            text = stringResource(id = R.string.btn_resend_otp),
            onClick = onResendOtp,
            enabled = !isLoading,
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onBackToPhone,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.btn_back_to_phone),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}
