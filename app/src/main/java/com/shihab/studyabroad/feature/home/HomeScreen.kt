package com.shihab.studyabroad.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.shihab.studyabroad.core.designsystem.component.OfflineBanner
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.data.repository.SampleData
import com.shihab.studyabroad.feature.home.components.DeadlineSoonSection
import com.shihab.studyabroad.feature.home.components.ProgressCard
import com.shihab.studyabroad.feature.home.components.QuickSummarySection

@Composable
fun HomeRoute(
    onNavigateToScholarshipDetail: (String) -> Unit = {},
    onNavigateToCountryDetail: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState,
        onScholarshipClick = onNavigateToScholarshipDetail,
        onCountryClick = onNavigateToCountryDetail,
        onRetry = {},
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    onScholarshipClick: (String) -> Unit = {},
    onCountryClick: (String) -> Unit = {},
    onRetry: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            AppTopBar(title = stringResource(id = R.string.app_name))
        },
        modifier = modifier,
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)
        when (uiState) {
            is HomeUiState.Loading -> AppLoadingWheel(modifier = contentModifier)
            is HomeUiState.Empty -> AppEmptyContent(modifier = contentModifier)
            is HomeUiState.Error -> AppErrorContent(
                message = uiState.message.asString(),
                onRetry = onRetry,
                modifier = contentModifier,
            )
            is HomeUiState.Content -> HomeContent(
                data = uiState.data,
                onScholarshipClick = onScholarshipClick,
                onCountryClick = onCountryClick,
                modifier = contentModifier,
            )
            is HomeUiState.OfflineStale -> Column(modifier = contentModifier) {
                OfflineBanner(lastUpdated = "2025-01-15")
                HomeContent(
                    data = uiState.data,
                    onScholarshipClick = onScholarshipClick,
                    onCountryClick = onCountryClick,
                )
            }
        }
    }
}

@Composable
private fun HomeContent(
    data: HomeData,
    onScholarshipClick: (String) -> Unit,
    onCountryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ProgressCard(progressPercentage = data.progressPercentage)

        QuickSummarySection(
            fullyFunded = data.topFullyFundedScholarships,
            lowCost = data.topLowCostCountries,
            prFriendly = data.topPrFriendlyCountries,
            onScholarshipClick = onScholarshipClick,
            onCountryClick = onCountryClick,
        )

        if (data.deadlineSoonScholarships.isNotEmpty()) {
            DeadlineSoonSection(
                scholarships = data.deadlineSoonScholarships,
                onScholarshipClick = onScholarshipClick,
            )
        }

        Text(
            text = stringResource(id = R.string.sample_data_disclaimer),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@ThemePreviews
@Composable
fun HomeScreenContentPreview() {
    StudyAbroadTheme {
        HomeScreen(
            uiState = HomeUiState.Content(
                HomeData(
                    progressPercentage = 60,
                    topFullyFundedScholarships = SampleData.sampleScholarships.take(3),
                    topLowCostCountries = SampleData.sampleCountries.take(3),
                    topPrFriendlyCountries = SampleData.sampleCountries.take(3),
                    deadlineSoonScholarships = SampleData.sampleScholarships.take(3),
                ),
            ),
        )
    }
}

@ThemePreviews
@Composable
fun HomeScreenLoadingPreview() {
    StudyAbroadTheme {
        HomeScreen(uiState = HomeUiState.Loading)
    }
}

@ThemePreviews
@Composable
fun HomeScreenEmptyPreview() {
    StudyAbroadTheme {
        HomeScreen(uiState = HomeUiState.Empty)
    }
}
