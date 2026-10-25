package com.shihab.studyabroad.feature.profile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.common.LanguageManager
import com.shihab.studyabroad.core.designsystem.component.AppLoadingWheel
import com.shihab.studyabroad.core.designsystem.component.InfoCard
import com.shihab.studyabroad.core.designsystem.component.LabeledValue
import com.shihab.studyabroad.core.designsystem.component.PrimaryButton
import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.TestType

@Composable
fun ProfileRoute(
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToNotificationSettings: () -> Unit = {},
    onNavigateToDisclaimer: () -> Unit = {},
    onNavigateToAuth: () -> Unit = {},
    onNavigateToThemePreview: (() -> Unit)? = null,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ProfileScreen(
        uiState = uiState,
        onNavigateToEditProfile = onNavigateToEditProfile,
        onNavigateToNotificationSettings = onNavigateToNotificationSettings,
        onNavigateToDisclaimer = onNavigateToDisclaimer,
        onDeleteAccount = viewModel::onDeleteAccount,
        onNavigateToAuth = onNavigateToAuth,
        onNavigateToThemePreview = onNavigateToThemePreview,
    )
}

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    modifier: Modifier = Modifier,
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToNotificationSettings: () -> Unit = {},
    onNavigateToDisclaimer: () -> Unit = {},
    onDeleteAccount: ((Boolean, UiText?) -> Unit) -> Unit = { _ -> },
    onNavigateToAuth: () -> Unit = {},
    onNavigateToThemePreview: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    var currentLang by remember { mutableStateOf(LanguageManager.getCurrentLanguage()) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val deleteSuccessMessage = stringResource(id = R.string.me_delete_account_success)

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = stringResource(id = R.string.me_delete_account_dialog_title),
                    style = MaterialTheme.typography.titleMedium,
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.me_delete_account_dialog_msg),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteAccount { success, errorMsg ->
                            if (success) {
                                Toast.makeText(context, deleteSuccessMessage, Toast.LENGTH_SHORT).show()
                                onNavigateToAuth()
                            } else if (errorMsg != null) {
                                Toast.makeText(context, errorMsg.asString(context), Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ),
                    modifier = Modifier.heightIn(min = 48.dp),
                ) {
                    Text(text = stringResource(id = R.string.me_delete_account_confirm))
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteDialog = false },
                    modifier = Modifier.heightIn(min = 48.dp),
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.nav_me),
            style = MaterialTheme.typography.headlineMedium,
        )

        when (uiState) {
            is ProfileUiState.Loading -> AppLoadingWheel()
            is ProfileUiState.Content -> {
                val profile = uiState.userProfile

                // Section 1: Profile Summary Card (FR-PRO-003)
                InfoCard(title = stringResource(id = R.string.me_profile_summary)) {
                    if (profile != null) {
                        LabeledValue(
                            label = "Track",
                            value = profile.targetTrack.name,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        LabeledValue(
                            label = stringResource(id = R.string.label_degree_level),
                            value = profile.level,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        LabeledValue(
                            label = stringResource(id = R.string.label_subject),
                            value = profile.subject,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (profile.cgpa != null) {
                            LabeledValue(
                                label = stringResource(id = R.string.label_cgpa),
                                value = "${profile.cgpa} / ${profile.cgpaScale}",
                            )
                        }
                        if (profile.testType != TestType.NONE && profile.testScore != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            LabeledValue(
                                label = profile.testType.name,
                                value = profile.testScore.toString(),
                            )
                        }
                        if (profile.targetCountries.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            LabeledValue(
                                label = stringResource(id = R.string.label_target_countries),
                                value = profile.targetCountries.joinToString(", "),
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        PrimaryButton(
                            text = stringResource(id = R.string.btn_edit_profile),
                            onClick = onNavigateToEditProfile,
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.state_empty),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        PrimaryButton(
                            text = stringResource(id = R.string.btn_edit_profile),
                            onClick = onNavigateToEditProfile,
                        )
                    }
                }

                // Section 2: Language Setting Card (FR-PRO-004)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.me_language_label),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .clickable {
                                    LanguageManager.setLanguage("en")
                                    currentLang = "en"
                                },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = currentLang == "en",
                                onClick = {
                                    LanguageManager.setLanguage("en")
                                    currentLang = "en"
                                },
                            )
                            Text(
                                text = stringResource(id = R.string.me_language_en),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 8.dp),
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .clickable {
                                    LanguageManager.setLanguage("bn")
                                    currentLang = "bn"
                                },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = currentLang.startsWith("bn"),
                                onClick = {
                                    LanguageManager.setLanguage("bn")
                                    currentLang = "bn"
                                },
                            )
                            Text(
                                text = stringResource(id = R.string.me_language_bn),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 8.dp),
                            )
                        }
                    }
                }

                // Section 3: Notification Settings Link Card (FR-NTF-004)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToNotificationSettings() },
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.me_notification_settings),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(id = R.string.notifications_deadline_reminders_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                // Section 4: Notice & Disclaimer Card (FR-TRU-005)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToDisclaimer() },
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.me_disclaimer_title),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(id = R.string.disclaimer_body),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.me_view_disclaimer),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }

                // Section 5: Delete Account Placeholder (FR-AUTH-004)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.me_delete_account),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(id = R.string.me_delete_account_dialog_msg),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { showDeleteDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp),
                        ) {
                            Text(text = stringResource(id = R.string.me_delete_account_confirm))
                        }
                    }
                }

                // Section 6: Debug Theme Preview (Debug builds only)
                if (onNavigateToThemePreview != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToThemePreview() },
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                        ) {
                            Text(
                                text = "Design System & Theme Preview",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Inspect all components, tokens, and dark/light modes",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                // Section 7: App Version
                Text(
                    text = stringResource(id = R.string.me_app_version, uiState.appVersion),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        }
    }
}
