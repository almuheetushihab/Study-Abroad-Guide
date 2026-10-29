package com.shihab.studyabroad.feature.saved

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.shihab.studyabroad.core.ui.PreviewData
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.domain.model.PopulatedSavedItems
import com.shihab.studyabroad.domain.model.SavedType
import com.shihab.studyabroad.feature.saved.components.SavedGroupSection
import com.shihab.studyabroad.feature.saved.components.SavedItemCard
import kotlinx.coroutines.launch

@Composable
fun SavedRoute(
    modifier: Modifier = Modifier,
    onNavigateToCountryDetail: (String) -> Unit = {},
    onNavigateToUniversityDetail: (String) -> Unit = {},
    onNavigateToScholarshipDetail: (String) -> Unit = {},
    viewModel: SavedViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SavedScreen(
        uiState = uiState,
        onNavigateToCountryDetail = onNavigateToCountryDetail,
        onNavigateToUniversityDetail = onNavigateToUniversityDetail,
        onNavigateToScholarshipDetail = onNavigateToScholarshipDetail,
        onUnsaveItem = viewModel::unsaveItem,
        onRestoreItem = viewModel::restoreItem,
        onRetry = {},
        modifier = modifier,
    )
}

@Composable
fun SavedScreen(
    uiState: SavedUiState,
    modifier: Modifier = Modifier,
    onNavigateToCountryDetail: (String) -> Unit = {},
    onNavigateToUniversityDetail: (String) -> Unit = {},
    onNavigateToScholarshipDetail: (String) -> Unit = {},
    onUnsaveItem: (SavedType, String) -> Unit = { _, _ -> },
    onRestoreItem: (SavedType, String) -> Unit = { _, _ -> },
    onRetry: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val removedMessage = stringResource(id = R.string.item_removed_message)
    val undoLabel = stringResource(id = R.string.btn_undo)

    val handleUnsave: (SavedType, String) -> Unit = { type, itemId ->
        onUnsaveItem(type, itemId)
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            val result = snackbarHostState.showSnackbar(
                message = removedMessage,
                actionLabel = undoLabel,
                duration = SnackbarDuration.Short,
            )
            if (result == SnackbarResult.ActionPerformed) {
                onRestoreItem(type, itemId)
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(id = R.string.nav_saved),
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier,
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)
        when (uiState) {
            is SavedUiState.Loading -> AppLoadingWheel(modifier = contentModifier)

            is SavedUiState.Empty -> AppEmptyContent(
                message = stringResource(id = R.string.saved_empty_desc),
                modifier = contentModifier,
            )

            is SavedUiState.Error -> AppErrorContent(
                message = uiState.message.asString(),
                onRetry = onRetry,
                modifier = contentModifier,
            )

            is SavedUiState.Content -> SavedContent(
                data = uiState.data,
                onNavigateToCountryDetail = onNavigateToCountryDetail,
                onNavigateToUniversityDetail = onNavigateToUniversityDetail,
                onNavigateToScholarshipDetail = onNavigateToScholarshipDetail,
                onUnsave = handleUnsave,
                modifier = contentModifier,
            )

            is SavedUiState.OfflineStale -> Column(modifier = contentModifier) {
                OfflineBanner(lastUpdated = "2025-01-15")
                SavedContent(
                    data = uiState.data,
                    onNavigateToCountryDetail = onNavigateToCountryDetail,
                    onNavigateToUniversityDetail = onNavigateToUniversityDetail,
                    onNavigateToScholarshipDetail = onNavigateToScholarshipDetail,
                    onUnsave = handleUnsave,
                )
            }
        }
    }
}

@Composable
private fun SavedContent(
    data: PopulatedSavedItems,
    onNavigateToCountryDetail: (String) -> Unit,
    onNavigateToUniversityDetail: (String) -> Unit,
    onNavigateToScholarshipDetail: (String) -> Unit,
    onUnsave: (SavedType, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (data.countries.isNotEmpty()) {
            item {
                SavedGroupSection(title = stringResource(id = R.string.saved_group_countries)) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        data.countries.forEach { country ->
                            SavedItemCard(
                                title = country.name,
                                subtitle = country.tuitionRangeText,
                                onClick = { onNavigateToCountryDetail(country.id) },
                                onUnsave = { onUnsave(SavedType.COUNTRY, country.id) },
                            )
                        }
                    }
                }
            }
        }

        if (data.universities.isNotEmpty()) {
            item {
                SavedGroupSection(title = stringResource(id = R.string.saved_group_universities)) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        data.universities.forEach { university ->
                            SavedItemCard(
                                title = university.name,
                                subtitle = university.tuitionFeeText ?: "",
                                onClick = { onNavigateToUniversityDetail(university.id) },
                                onUnsave = { onUnsave(SavedType.UNIVERSITY, university.id) },
                                badgeText = university.worldRank?.let { "Rank #$it" },
                            )
                        }
                    }
                }
            }
        }

        if (data.scholarships.isNotEmpty()) {
            item {
                SavedGroupSection(title = stringResource(id = R.string.saved_group_scholarships)) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        data.scholarships.forEach { scholarship ->
                            SavedItemCard(
                                title = scholarship.name,
                                subtitle = "Deadline: ${scholarship.deadline}",
                                onClick = { onNavigateToScholarshipDetail(scholarship.id) },
                                onUnsave = { onUnsave(SavedType.SCHOLARSHIP, scholarship.id) },
                                badgeText = scholarship.funding.name,
                            )
                        }
                    }
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun SavedScreenPreview() {
    StudyAbroadTheme {
        SavedScreen(
            uiState = SavedUiState.Content(
                data = PopulatedSavedItems(
                    countries = PreviewData.sampleCountries.take(2),
                    universities = PreviewData.sampleUniversities.take(2),
                    scholarships = PreviewData.sampleScholarships.take(2),
                ),
            ),
        )
    }
}
