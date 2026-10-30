package com.shihab.studyabroad.feature.country

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.shihab.studyabroad.core.designsystem.component.OfflineBanner
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.PreviewData
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.feature.country.components.ComparisonSortColumn
import com.shihab.studyabroad.feature.country.components.CountryComparisonTable
import com.shihab.studyabroad.feature.country.components.CountryCostBarChart

@Composable
fun CountryComparisonRoute(onBack: () -> Unit = {}, viewModel: CountryComparisonViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CountryComparisonScreen(
        uiState = uiState,
        onToggleCountry = viewModel::onToggleCountrySelection,
        onSortColumnClick = viewModel::onSortColumnClick,
        onBack = onBack,
        onRetry = {},
    )
}

@Composable
fun CountryComparisonScreen(
    uiState: CountryComparisonUiState,
    modifier: Modifier = Modifier,
    onToggleCountry: (String) -> Unit = {},
    onSortColumnClick: (ComparisonSortColumn) -> Unit = {},
    onBack: () -> Unit = {},
    onRetry: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(id = R.string.country_comparison_title),
                onBack = onBack,
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)
        when (uiState) {
            is CountryComparisonUiState.Loading -> AppLoadingWheel(modifier = contentModifier)

            is CountryComparisonUiState.Empty -> AppEmptyContent(modifier = contentModifier)

            is CountryComparisonUiState.Error -> AppErrorContent(
                message = uiState.message.asString(),
                onRetry = onRetry,
                modifier = contentModifier,
            )

            is CountryComparisonUiState.Content -> ComparisonContent(
                data = uiState.data,
                onToggleCountry = onToggleCountry,
                onSortColumnClick = onSortColumnClick,
                modifier = contentModifier,
            )

            is CountryComparisonUiState.OfflineStale -> Column(modifier = contentModifier) {
                OfflineBanner(lastUpdated = "2025-01-15")
                ComparisonContent(
                    data = uiState.data,
                    onToggleCountry = onToggleCountry,
                    onSortColumnClick = onSortColumnClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ComparisonContent(
    data: CountryComparisonData,
    onToggleCountry: (String) -> Unit,
    onSortColumnClick: (ComparisonSortColumn) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.country_compare_select_hint),
            style = MaterialTheme.typography.titleSmall,
        )

        // Select chips for up to 3 countries
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            data.availableCountries.forEach { country ->
                val isSelected = data.selectedCountryIds.contains(country.id)
                FilterChip(
                    selected = isSelected,
                    onClick = { onToggleCountry(country.id) },
                    label = { Text(text = country.name) },
                    modifier = Modifier.heightIn(min = 48.dp),
                )
            }
        }

        // Bar Chart
        CountryCostBarChart(selectedCountries = data.comparedCountries)

        // Sortable Table
        CountryComparisonTable(
            selectedCountries = data.comparedCountries,
            sortColumn = data.sortColumn,
            onSortColumnClick = onSortColumnClick,
        )
    }
}

@ThemePreviews
@Composable
fun CountryComparisonScreenPreview() {
    StudyAbroadTheme {
        CountryComparisonScreen(
            uiState = CountryComparisonUiState.Content(
                CountryComparisonData(
                    availableCountries = PreviewData.sampleCountries,
                    selectedCountryIds = PreviewData.sampleCountries.take(3).map { it.id }.toSet(),
                    comparedCountries = PreviewData.sampleCountries.take(3),
                ),
            ),
        )
    }
}
