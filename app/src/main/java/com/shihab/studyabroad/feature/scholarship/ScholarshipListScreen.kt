package com.shihab.studyabroad.feature.scholarship

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.designsystem.component.AppEmptyContent
import com.shihab.studyabroad.core.designsystem.component.AppErrorContent
import com.shihab.studyabroad.core.designsystem.component.AppLoadingWheel
import com.shihab.studyabroad.core.designsystem.component.AppTopBar
import com.shihab.studyabroad.core.designsystem.component.FilterChipRow
import com.shihab.studyabroad.core.designsystem.component.OfflineBanner
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.PreviewData
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.domain.model.ScholarshipStatus
import com.shihab.studyabroad.feature.scholarship.components.ScholarshipCard

@Composable
fun ScholarshipListRoute(onNavigateToScholarshipDetail: (String) -> Unit = {}, viewModel: ScholarshipListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ScholarshipListScreen(
        uiState = uiState,
        onSelectCountryFilter = viewModel::onSelectCountryFilter,
        onSelectLevelFilter = viewModel::onSelectLevelFilter,
        onSelectFundingFilter = viewModel::onSelectFundingFilter,
        onSelectStatusFilter = viewModel::onSelectStatusFilter,
        onScholarshipClick = onNavigateToScholarshipDetail,
        onRetry = {},
    )
}

@Composable
fun ScholarshipListScreen(
    uiState: ScholarshipListUiState,
    modifier: Modifier = Modifier,
    onSelectCountryFilter: (String) -> Unit = {},
    onSelectLevelFilter: (String) -> Unit = {},
    onSelectFundingFilter: (String) -> Unit = {},
    onSelectStatusFilter: (String) -> Unit = {},
    onScholarshipClick: (String) -> Unit = {},
    onRetry: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            AppTopBar(title = stringResource(id = R.string.nav_scholarships))
        },
        modifier = modifier,
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)
        when (uiState) {
            is ScholarshipListUiState.Loading -> AppLoadingWheel(modifier = contentModifier)

            is ScholarshipListUiState.Empty -> AppEmptyContent(modifier = contentModifier)

            is ScholarshipListUiState.Error -> AppErrorContent(
                message = uiState.message.asString(),
                onRetry = onRetry,
                modifier = contentModifier,
            )

            is ScholarshipListUiState.Content -> ScholarshipListContent(
                data = uiState.data,
                onSelectCountryFilter = onSelectCountryFilter,
                onSelectLevelFilter = onSelectLevelFilter,
                onSelectFundingFilter = onSelectFundingFilter,
                onSelectStatusFilter = onSelectStatusFilter,
                onScholarshipClick = onScholarshipClick,
                modifier = contentModifier,
            )

            is ScholarshipListUiState.OfflineStale -> Column(modifier = contentModifier) {
                OfflineBanner(lastUpdated = "2025-01-15")
                ScholarshipListContent(
                    data = uiState.data,
                    onSelectCountryFilter = onSelectCountryFilter,
                    onSelectLevelFilter = onSelectLevelFilter,
                    onSelectFundingFilter = onSelectFundingFilter,
                    onSelectStatusFilter = onSelectStatusFilter,
                    onScholarshipClick = onScholarshipClick,
                )
            }
        }
    }
}

@Composable
private fun ScholarshipListContent(
    data: ScholarshipListData,
    onSelectCountryFilter: (String) -> Unit,
    onSelectLevelFilter: (String) -> Unit,
    onSelectFundingFilter: (String) -> Unit,
    onSelectStatusFilter: (String) -> Unit,
    onScholarshipClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        FilterChipRow(
            options = data.availableCountries,
            selectedOption = data.selectedCountryFilter,
            onSelectOption = onSelectCountryFilter,
        )

        FilterChipRow(
            options = data.availableLevels,
            selectedOption = data.selectedLevelFilter,
            onSelectOption = onSelectLevelFilter,
        )

        FilterChipRow(
            options = data.availableFundings,
            selectedOption = data.selectedFundingFilter,
            onSelectOption = onSelectFundingFilter,
        )

        FilterChipRow(
            options = data.availableStatuses,
            selectedOption = data.selectedStatusFilter,
            onSelectOption = onSelectStatusFilter,
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(data.items, key = { it.scholarship.id }) { item ->
                ScholarshipCard(
                    scholarship = item.scholarship,
                    status = item.status,
                    onClick = { onScholarshipClick(item.scholarship.id) },
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun ScholarshipListScreenContentPreview() {
    StudyAbroadTheme {
        ScholarshipListScreen(
            uiState = ScholarshipListUiState.Content(
                ScholarshipListData(
                    items = PreviewData.sampleScholarships.map {
                        ScholarshipItemState(it, ScholarshipStatus.OPEN)
                    },
                ),
            ),
        )
    }
}
