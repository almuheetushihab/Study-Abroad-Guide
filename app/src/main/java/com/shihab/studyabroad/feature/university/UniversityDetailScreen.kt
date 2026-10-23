package com.shihab.studyabroad.feature.university

import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
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
import com.shihab.studyabroad.core.designsystem.component.ReportWrongInfoDialog
import com.shihab.studyabroad.core.designsystem.component.StaleBadge
import com.shihab.studyabroad.core.designsystem.component.VerifiedFooter
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.data.repository.SampleData
import com.shihab.studyabroad.domain.model.RequirementStatus
import com.shihab.studyabroad.domain.usecase.RequirementComparisonResult
import com.shihab.studyabroad.feature.university.components.RequirementMeter

@Composable
fun UniversityDetailRoute(onBack: () -> Unit = {}, viewModel: UniversityDetailViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    UniversityDetailScreen(
        uiState = uiState,
        onBack = onBack,
        onToggleSave = viewModel::onToggleSave,
        onSubmitReport = viewModel::submitReport,
        onOpenWebsite = viewModel::openWebsite,
        onRetry = {},
    )
}

@Composable
fun UniversityDetailScreen(
    uiState: UniversityDetailUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onToggleSave: () -> Unit = {},
    onSubmitReport: (String, (Boolean) -> Unit) -> Unit = { _, _ -> },
    onOpenWebsite: (String) -> Unit = {},
    onRetry: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = when (uiState) {
                    is UniversityDetailUiState.Content -> uiState.data.university.name
                    else -> stringResource(id = R.string.nav_universities)
                },
                onBack = onBack,
                actions = {
                    if (uiState is UniversityDetailUiState.Content) {
                        val isSaved = uiState.data.isSaved
                        IconButton(
                            onClick = onToggleSave,
                            modifier = Modifier.heightIn(min = 48.dp),
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = if (isSaved) stringResource(R.string.btn_saved) else stringResource(R.string.btn_save),
                                tint = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)
        when (uiState) {
            is UniversityDetailUiState.Loading -> AppLoadingWheel(modifier = contentModifier)
            is UniversityDetailUiState.Empty -> AppEmptyContent(modifier = contentModifier)
            is UniversityDetailUiState.Error -> AppErrorContent(
                message = uiState.message.asString(),
                onRetry = onRetry,
                modifier = contentModifier,
            )
            is UniversityDetailUiState.Content -> UniversityDetailContent(
                data = uiState.data,
                onSubmitReport = onSubmitReport,
                onOpenWebsite = onOpenWebsite,
                modifier = contentModifier,
            )
            is UniversityDetailUiState.OfflineStale -> Column(modifier = contentModifier) {
                OfflineBanner(lastUpdated = "2025-01-15")
                UniversityDetailContent(
                    data = uiState.data,
                    onSubmitReport = onSubmitReport,
                    onOpenWebsite = onOpenWebsite,
                )
            }
        }
    }
}

@Composable
private fun UniversityDetailContent(
    data: UniversityDetailData,
    onSubmitReport: (String, (Boolean) -> Unit) -> Unit,
    onOpenWebsite: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val university = data.university
    val context = LocalContext.current
    var showReportDialog by remember { mutableStateOf(false) }
    val reportSubmittedMessage = stringResource(id = R.string.report_submitted_message)

    if (showReportDialog) {
        ReportWrongInfoDialog(
            onDismiss = { showReportDialog = false },
            onSubmit = { message ->
                onSubmitReport(message) { success ->
                    if (success) {
                        Toast.makeText(context, reportSubmittedMessage, Toast.LENGTH_SHORT).show()
                    }
                    showReportDialog = false
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
        // Name & Rank
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = university.name,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f),
            )
            if (university.worldRank != null) {
                SuggestionChip(
                    onClick = {},
                    label = { Text(text = stringResource(R.string.label_world_rank, university.worldRank)) },
                    modifier = Modifier.heightIn(min = 32.dp),
                )
            }
        }

        // Stale Badge if > 90 days
        if (data.isStale) {
            StaleBadge()
        }

        // Programs List
        if (university.programs.isNotEmpty()) {
            InfoCard(title = stringResource(R.string.university_programs_title)) {
                university.programs.forEach { program ->
                    Text(text = "• $program", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // Requirement vs Profile Meter
        RequirementMeter(
            comparison = data.requirementComparison,
            minCgpa = university.minCgpa,
            minIelts = university.minIelts,
            minExp = university.minExpYears,
        )

        // Tuition & Deadline
        InfoCard(title = "Key Details") {
            if (!university.tuitionFeeText.isNullOrBlank()) {
                Text(
                    text = "${stringResource(R.string.label_tuition)}: ${university.tuitionFeeText}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            if (university.deadline != null) {
                Text(
                    text = "${stringResource(R.string.label_deadline)}: ${university.deadline}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        // Official Website Button
        Button(
            onClick = { onOpenWebsite(university.websiteUrl) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
        ) {
            Text(text = stringResource(R.string.btn_official_website))
        }

        // Report Wrong Info Button
        OutlinedButton(
            onClick = { showReportDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
        ) {
            Text(text = stringResource(id = R.string.action_report_wrong_info))
        }

        // Verified Footer
        VerifiedFooter(
            sourceUrl = university.sourceUrl,
            verifiedOn = university.verifiedOn.toString(),
            onOpenSource = onOpenWebsite,
        )
    }
}

@ThemePreviews
@Composable
fun UniversityDetailScreenPreview() {
    StudyAbroadTheme {
        UniversityDetailScreen(
            uiState = UniversityDetailUiState.Content(
                UniversityDetailData(
                    university = SampleData.sampleUniversities.first(),
                    requirementComparison = RequirementComparisonResult(
                        cgpaStatus = RequirementStatus.OK,
                        testStatus = RequirementStatus.LOW,
                        expStatus = RequirementStatus.UNKNOWN,
                    ),
                    isStale = false,
                ),
            ),
        )
    }
}
