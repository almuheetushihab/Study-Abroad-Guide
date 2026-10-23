package com.shihab.studyabroad.feature.country

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.shihab.studyabroad.core.designsystem.component.StaleBadge
import com.shihab.studyabroad.core.designsystem.component.VerifiedFooter
import com.shihab.studyabroad.core.designsystem.component.WarningBanner
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.data.repository.SampleData
import com.shihab.studyabroad.domain.model.CountryTier

@Composable
fun CountryDetailRoute(
    onBack: () -> Unit = {},
    onNavigateToScholarshipDetail: (String) -> Unit = {},
    onNavigateToUniversityDetail: (String) -> Unit = {},
    viewModel: CountryDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CountryDetailScreen(
        uiState = uiState,
        onBack = onBack,
        onScholarshipClick = onNavigateToScholarshipDetail,
        onUniversityClick = onNavigateToUniversityDetail,
        onToggleSave = viewModel::onToggleSave,
        onOpenSource = viewModel::openSourceUrl,
        onRetry = {},
    )
}

@Composable
fun CountryDetailScreen(
    uiState: CountryDetailUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onScholarshipClick: (String) -> Unit = {},
    onUniversityClick: (String) -> Unit = {},
    onToggleSave: () -> Unit = {},
    onOpenSource: (String) -> Unit = {},
    onRetry: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = when (uiState) {
                    is CountryDetailUiState.Content -> uiState.data.country.name
                    else -> stringResource(id = R.string.nav_countries)
                },
                onBack = onBack,
                actions = {
                    if (uiState is CountryDetailUiState.Content) {
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
            is CountryDetailUiState.Loading -> AppLoadingWheel(modifier = contentModifier)
            is CountryDetailUiState.Empty -> AppEmptyContent(modifier = contentModifier)
            is CountryDetailUiState.Error -> AppErrorContent(
                message = uiState.message.asString(),
                onRetry = onRetry,
                modifier = contentModifier,
            )
            is CountryDetailUiState.Content -> CountryDetailContent(
                data = uiState.data,
                onScholarshipClick = onScholarshipClick,
                onUniversityClick = onUniversityClick,
                onOpenSource = onOpenSource,
                modifier = contentModifier,
            )
            is CountryDetailUiState.OfflineStale -> Column(modifier = contentModifier) {
                OfflineBanner(lastUpdated = "2025-01-15")
                CountryDetailContent(
                    data = uiState.data,
                    onScholarshipClick = onScholarshipClick,
                    onUniversityClick = onUniversityClick,
                    onOpenSource = onOpenSource,
                )
            }
        }
    }
}

@Composable
private fun CountryDetailContent(
    data: CountryDetailData,
    onScholarshipClick: (String) -> Unit,
    onUniversityClick: (String) -> Unit,
    onOpenSource: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val country = data.country

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Tier Header Tag
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = country.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            val tierLabel = when (country.tier) {
                CountryTier.SCHOLARSHIP -> stringResource(R.string.country_tier_scholarship)
                CountryTier.PR_WORK -> stringResource(R.string.country_tier_pr_work)
            }
            SuggestionChip(
                onClick = {},
                label = { Text(text = tierLabel) },
                modifier = Modifier.heightIn(min = 36.dp),
            )
        }

        // Stale Badge if > 90 days
        if (data.isStale) {
            StaleBadge()
        }

        // Risk Advisory Banner
        val advisoryText = country.advisory
        if (!advisoryText.isNullOrBlank()) {
            WarningBanner(message = advisoryText)
        }

        // Key Stats Row Card
        InfoCard(title = "Key Statistics") {
            Text(
                text = "${stringResource(R.string.label_tuition)}: ${country.tuitionRangeText}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${stringResource(R.string.label_living_cost)}: ${country.livingRangeText}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${stringResource(R.string.label_work_rules)}: ${country.workRuleText}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        // Scholarships Box
        if (data.scholarships.isNotEmpty()) {
            InfoCard(title = stringResource(R.string.country_scholarships_title)) {
                data.scholarships.forEach { scholarship ->
                    Text(
                        text = "• ${scholarship.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onScholarshipClick(scholarship.id) }
                            .padding(vertical = 4.dp),
                    )
                }
            }
        }

        // Top Universities Box
        if (data.universities.isNotEmpty()) {
            InfoCard(title = stringResource(R.string.country_universities_title)) {
                data.universities.forEach { university ->
                    Text(
                        text = "• ${university.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onUniversityClick(university.id) }
                            .padding(vertical = 4.dp),
                    )
                }
            }
        }

        // Work and Post-Study Visa
        InfoCard(title = stringResource(R.string.country_work_post_study_title)) {
            Text(
                text = country.postStudyText,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        // Verified Footer
        VerifiedFooter(
            sourceUrl = country.sourceUrl,
            verifiedOn = country.verifiedOn.toString(),
            onOpenSource = onOpenSource,
        )
    }
}

@ThemePreviews
@Composable
fun CountryDetailScreenPreview() {
    StudyAbroadTheme {
        CountryDetailScreen(
            uiState = CountryDetailUiState.Content(
                CountryDetailData(
                    country = SampleData.sampleCountries.first(),
                    scholarships = SampleData.sampleScholarships.take(2),
                    universities = SampleData.sampleUniversities.take(2),
                    isStale = true,
                ),
            ),
        )
    }
}
