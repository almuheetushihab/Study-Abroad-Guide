package com.shihab.studyabroad.feature.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
fun PhoneLoginForm(
    phoneNumber: String,
    onPhoneNumberChanged: (String) -> Unit,
    onSendOtp: () -> Unit,
    onSignInWithGoogle: () -> Unit,
    isLoading: Boolean,
    errorMessage: UiText?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.login_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.login_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChanged,
            label = stringResource(id = R.string.login_phone_label),
            isError = errorMessage != null,
            supportingText = errorMessage?.asString() ?: stringResource(id = R.string.login_phone_placeholder),
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = stringResource(id = R.string.btn_send_otp),
            onClick = onSendOtp,
            enabled = !isLoading && phoneNumber.isNotBlank(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        SecondaryButton(
            text = stringResource(id = R.string.btn_google_sign_in),
            onClick = onSignInWithGoogle,
            enabled = !isLoading,
        )
    }
}
