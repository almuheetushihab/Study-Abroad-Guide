package com.shihab.studyabroad.feature.scholarship

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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.designsystem.component.AppEmptyContent
import com.shihab.studyabroad.core.designsystem.component.AppErrorContent
import com.shihab.studyabroad.core.designsystem.component.AppLoadingWheel
import com.shihab.studyabroad.core.designsystem.component.AppTopBar
import com.shihab.studyabroad.core.designsystem.component.InfoCard
import com.shihab.studyabroad.core.designsystem.component.OfflineBanner
import com.shihab.studyabroad.core.designsystem.component.ScamAlertCard
import com.shihab.studyabroad.core.designsystem.component.StaleBadge
import com.shihab.studyabroad.core.designsystem.component.StatusChip
import com.shihab.studyabroad.core.designsystem.component.VerifiedFooter
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.data.repository.SampleData
import com.shihab.studyabroad.domain.model.ScholarshipStatus
import com.shihab.studyabroad.feature.scholarship.components.ReminderDialog

@Composable
fun ScholarshipDetailRoute(onBack: () -> Unit = {}, viewModel: ScholarshipDetailViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ScholarshipDetailScreen(
        uiState = uiState,
        onBack = onBack,
        onSetReminderDays = viewModel::onSetReminderDays,
        onOpenApplyUrl = viewModel::openApplyUrl,
        onRetry = {},
    )
}

@Composable
fun ScholarshipDetailScreen(
    uiState: ScholarshipDetailUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onSetReminderDays: (Int) -> Unit = {},
    onOpenApplyUrl: (String) -> Unit = {},
    onRetry: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = when (uiState) {
                    is ScholarshipDetailUiState.Content -> uiState.data.scholarship.name
                    else -> stringResource(id = R.string.nav_scholarships)
                },
                onBack = onBack,
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)
        when (uiState) {
            is ScholarshipDetailUiState.Loading -> AppLoadingWheel(modifier = contentModifier)
            is ScholarshipDetailUiState.Empty -> AppEmptyContent(modifier = contentModifier)
            is ScholarshipDetailUiState.Error -> AppErrorContent(
                message = uiState.message.asString(),
                onRetry = onRetry,
                modifier = contentModifier,
            )
            is ScholarshipDetailUiState.Content -> ScholarshipDetailContent(
                data = uiState.data,
                onSetReminderDays = onSetReminderDays,
                onOpenApplyUrl = onOpenApplyUrl,
                modifier = contentModifier,
            )
            is ScholarshipDetailUiState.OfflineStale -> Column(modifier = contentModifier) {
                OfflineBanner(lastUpdated = "2025-01-15")
                ScholarshipDetailContent(
                    data = uiState.data,
                    onSetReminderDays = onSetReminderDays,
                    onOpenApplyUrl = onOpenApplyUrl,
                )
            }
        }
    }
}

@Composable
private fun ScholarshipDetailContent(
    data: ScholarshipDetailData,
    onSetReminderDays: (Int) -> Unit,
    onOpenApplyUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scholarship = data.scholarship
    var showReminderDialog by remember { mutableStateOf(false) }

    if (showReminderDialog) {
        ReminderDialog(
            onDismiss = { showReminderDialog = false },
            onSelectReminderDays = onSetReminderDays,
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Name & Status Chip
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = scholarship.name,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f),
            )
            StatusChip(status = data.status)
        }

        // Expected Next Cycle for Closed Scholarships (FR-SCH-004)
        if (data.status == ScholarshipStatus.CLOSED && data.expectedNextCycle != null) {
            SuggestionChip(
                onClick = {},
                label = { Text(text = data.expectedNextCycle) },
                modifier = Modifier.heightIn(min = 36.dp),
            )
        }

        // Stale Badge if > 90 days
        if (data.isStale) {
            StaleBadge()
        }

        // Eligibility Criteria Box (FR-SCH-005)
        InfoCard(title = stringResource(R.string.scholarship_eligibility_title)) {
            val elg = scholarship.eligibility
            val checkOfficial = stringResource(R.string.eligibility_check_official_site)

            Text(text = "Min CGPA: ${elg.minCgpa?.toString() ?: checkOfficial}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Min Test Score: ${elg.minIelts?.toString() ?: checkOfficial}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Min Work Experience: ${if (elg.minExpYears != null) "${elg.minExpYears} yrs" else checkOfficial}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Max Age: ${if (elg.maxAge != null) "${elg.maxAge} yrs" else checkOfficial}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        // Benefits & Coverage
        InfoCard(title = stringResource(R.string.scholarship_benefits_title)) {
            Text(
                text = "Funding Type: ${scholarship.funding.name}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Covers tuition, living stipend, monthly allowance, and travel grants depending on recipient track.",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        // Required Documents
        InfoCard(title = stringResource(R.string.scholarship_documents_title)) {
            Text(text = "• Academic Transcripts & Certificate", style = MaterialTheme.typography.bodyMedium)
            Text(text = "• IELTS / Language Certificate", style = MaterialTheme.typography.bodyMedium)
            Text(text = "• Statement of Purpose (SOP)", style = MaterialTheme.typography.bodyMedium)
            Text(text = "• 2 Recommendation Letters (LOR)", style = MaterialTheme.typography.bodyMedium)
        }

        // Deadline & Timezone Note (FR-SCH-006)
        InfoCard(title = stringResource(R.string.label_deadline)) {
            Text(text = "Open Date: ${scholarship.openDate}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Deadline: ${scholarship.deadline}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.scholarship_deadline_note),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
            )
        }

        // Set Reminder Button (UI confirmation)
        OutlinedButton(
            onClick = { showReminderDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
        ) {
            Text(text = stringResource(R.string.btn_set_reminder))
        }

        if (data.reminderConfirmationMessage != null) {
            Text(
                text = data.reminderConfirmationMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        // Official Apply Link Button
        Button(
            onClick = { onOpenApplyUrl(scholarship.applyUrl) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
        ) {
            Text(text = stringResource(R.string.btn_official_apply))
        }

        // Scam Alert Card (FR-TRU-004)
        ScamAlertCard()

        // Verified Footer (FR-TRU-001)
        VerifiedFooter(
            sourceUrl = scholarship.sourceUrl,
            verifiedOn = scholarship.verifiedOn.toString(),
            onOpenSource = onOpenApplyUrl,
        )
    }
}

@ThemePreviews
@Composable
fun ScholarshipDetailScreenPreview() {
    StudyAbroadTheme {
        ScholarshipDetailScreen(
            uiState = ScholarshipDetailUiState.Content(
                ScholarshipDetailData(
                    scholarship = SampleData.sampleScholarships.first(),
                    status = ScholarshipStatus.OPEN,
                    isStale = false,
                ),
            ),
        )
    }
}
