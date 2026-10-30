package com.shihab.studyabroad.feature.university

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import com.shihab.studyabroad.core.designsystem.component.FilterChipRow
import com.shihab.studyabroad.core.designsystem.component.OfflineBanner
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.PreviewData
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.feature.university.components.UniversityCard

@Composable
fun UniversityListRoute(onNavigateToUniversityDetail: (String) -> Unit = {}, viewModel: UniversityListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    UniversityListScreen(
        uiState = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onSelectCountryFilter = viewModel::onSelectCountryFilter,
        onSelectLevelFilter = viewModel::onSelectLevelFilter,
        onUniversityClick = onNavigateToUniversityDetail,
        onRetry = {},
    )
}

@Composable
fun UniversityListScreen(
    uiState: UniversityListUiState,
    modifier: Modifier = Modifier,
    onSearchQueryChange: (String) -> Unit = {},
    onSelectCountryFilter: (String) -> Unit = {},
    onSelectLevelFilter: (String) -> Unit = {},
    onUniversityClick: (String) -> Unit = {},
    onRetry: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            AppTopBar(title = stringResource(id = R.string.nav_universities))
        },
        modifier = modifier,
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)
        when (uiState) {
            is UniversityListUiState.Loading -> AppLoadingWheel(modifier = contentModifier)

            is UniversityListUiState.Empty -> AppEmptyContent(modifier = contentModifier)

            is UniversityListUiState.Error -> AppErrorContent(
                message = uiState.message.asString(),
                onRetry = onRetry,
                modifier = contentModifier,
            )

            is UniversityListUiState.Content -> UniversityListContent(
                data = uiState.data,
                onSearchQueryChange = onSearchQueryChange,
                onSelectCountryFilter = onSelectCountryFilter,
                onSelectLevelFilter = onSelectLevelFilter,
                onUniversityClick = onUniversityClick,
                modifier = contentModifier,
            )

            is UniversityListUiState.OfflineStale -> Column(modifier = contentModifier) {
                OfflineBanner(lastUpdated = "2025-01-15")
                UniversityListContent(
                    data = uiState.data,
                    onSearchQueryChange = onSearchQueryChange,
                    onSelectCountryFilter = onSelectCountryFilter,
                    onSelectLevelFilter = onSelectLevelFilter,
                    onUniversityClick = onUniversityClick,
                )
            }
        }
    }
}

@Composable
private fun UniversityListContent(
    data: UniversityListData,
    onSearchQueryChange: (String) -> Unit,
    onSelectCountryFilter: (String) -> Unit,
    onSelectLevelFilter: (String) -> Unit,
    onUniversityClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        // Search Input
        OutlinedTextField(
            value = data.searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text(text = stringResource(R.string.university_search_placeholder)) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .heightIn(min = 48.dp),
        )

        // Filters
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

        // List
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(data.universities, key = { it.id }) { university ->
                UniversityCard(
                    university = university,
                    onClick = { onUniversityClick(university.id) },
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun UniversityListScreenContentPreview() {
    StudyAbroadTheme {
        UniversityListScreen(
            uiState = UniversityListUiState.Content(
                UniversityListData(
                    universities = PreviewData.sampleUniversities,
                ),
            ),
        )
    }
}
