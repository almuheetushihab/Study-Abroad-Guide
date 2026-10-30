package com.shihab.studyabroad.feature.notifications

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.designsystem.component.AppTopBar
import com.shihab.studyabroad.core.designsystem.component.WarningBanner

@Composable
fun NotificationsRoute(onBack: () -> Unit = {}, viewModel: NotificationsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    NotificationsScreen(
        uiState = uiState,
        onBack = onBack,
        onToggleDeadlineReminders = viewModel::onToggleDeadlineReminders,
        onToggleNewScholarships = viewModel::onToggleNewScholarships,
        onToggleAppNews = viewModel::onToggleAppNews,
    )
}

@Composable
fun NotificationsScreen(
    uiState: NotificationsUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onToggleDeadlineReminders: (Boolean) -> Unit = {},
    onToggleNewScholarships: (Boolean) -> Unit = {},
    onToggleAppNews: (Boolean) -> Unit = {},
) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            },
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        hasPermission = isGranted
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(id = R.string.notifications_title),
                onBack = onBack,
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            // Android 13+ Permission Warning / Request
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasPermission) {
                WarningBanner(message = stringResource(id = R.string.notifications_permission_denied))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp),
                ) {
                    Text(text = stringResource(id = R.string.btn_grant_permission))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Toggle 1: Deadline Reminders
            NotificationSettingItem(
                title = stringResource(id = R.string.notifications_deadline_reminders),
                description = stringResource(id = R.string.notifications_deadline_reminders_desc),
                checked = uiState.deadlineRemindersEnabled,
                onCheckedChange = { checked ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasPermission) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    onToggleDeadlineReminders(checked)
                },
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Toggle 2: New Scholarships
            NotificationSettingItem(
                title = stringResource(id = R.string.notifications_new_scholarships),
                description = stringResource(id = R.string.notifications_new_scholarships_desc),
                checked = uiState.newScholarshipsEnabled,
                onCheckedChange = { checked ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasPermission) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    onToggleNewScholarships(checked)
                },
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Toggle 3: App News
            NotificationSettingItem(
                title = stringResource(id = R.string.notifications_app_news),
                description = stringResource(id = R.string.notifications_app_news_desc),
                checked = uiState.appNewsEnabled,
                onCheckedChange = { checked ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasPermission) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    onToggleAppNews(checked)
                },
            )
        }
    }
}

@Composable
private fun NotificationSettingItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.heightIn(min = 48.dp),
            )
        }
    }
}
