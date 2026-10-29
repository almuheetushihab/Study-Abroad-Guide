package com.shihab.studyabroad.feature.country

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.shihab.studyabroad.feature.country.components.CountryCard

@Composable
fun CountryListRoute(
    onNavigateToCountryDetail: (String) -> Unit = {},
    onNavigateToCountryComparison: () -> Unit = {},
    viewModel: CountryListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CountryListScreen(
        uiState = uiState,
        onSelectTierFilter = viewModel::onSelectTierFilter,
        onCountryClick = onNavigateToCountryDetail,
        onCompareClick = onNavigateToCountryComparison,
        onRetry = {},
    )
}

@Composable
fun CountryListScreen(
    uiState: CountryListUiState,
    modifier: Modifier = Modifier,
    onSelectTierFilter: (String) -> Unit = {},
    onCountryClick: (String) -> Unit = {},
    onCompareClick: () -> Unit = {},
    onRetry: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(id = R.string.nav_countries),
                actions = {
                    IconButton(onClick = onCompareClick) {
                        Icon(
                            imageVector = Icons.Default.Compare,
                            contentDescription = stringResource(R.string.btn_compare_countries),
                        )
                    }
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)
        when (uiState) {
            is CountryListUiState.Loading -> AppLoadingWheel(modifier = contentModifier)

            is CountryListUiState.Empty -> AppEmptyContent(modifier = contentModifier)

            is CountryListUiState.Error -> AppErrorContent(
                message = uiState.message.asString(),
                onRetry = onRetry,
                modifier = contentModifier,
            )

            is CountryListUiState.Content -> CountryListContent(
                data = uiState.data,
                onSelectTierFilter = onSelectTierFilter,
                onCountryClick = onCountryClick,
                modifier = contentModifier,
            )

            is CountryListUiState.OfflineStale -> Column(modifier = contentModifier) {
                OfflineBanner(lastUpdated = "2025-01-15")
                CountryListContent(
                    data = uiState.data,
                    onSelectTierFilter = onSelectTierFilter,
                    onCountryClick = onCountryClick,
                )
            }
        }
    }
}

@Composable
private fun CountryListContent(
    data: CountryListData,
    onSelectTierFilter: (String) -> Unit,
    onCountryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        FilterChipRow(
            options = data.availableTiers,
            selectedOption = data.selectedTierFilter,
            onSelectOption = onSelectTierFilter,
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(data.countries, key = { it.id }) { country ->
                CountryCard(
                    country = country,
                    onClick = { onCountryClick(country.id) },
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun CountryListScreenContentPreview() {
    StudyAbroadTheme {
        CountryListScreen(
            uiState = CountryListUiState.Content(
                CountryListData(
                    countries = PreviewData.sampleCountries,
                ),
            ),
        )
    }
}

@ThemePreviews
@Composable
fun CountryListScreenLoadingPreview() {
    StudyAbroadTheme {
        CountryListScreen(uiState = CountryListUiState.Loading)
    }
}
