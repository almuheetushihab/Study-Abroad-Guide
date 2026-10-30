package com.shihab.studyabroad.feature.documents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import com.shihab.studyabroad.core.designsystem.component.OfflineBanner
import com.shihab.studyabroad.core.designsystem.component.ProgressBarWithLabel
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.PreviewData
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.feature.documents.components.AttestationStepsSection
import com.shihab.studyabroad.feature.documents.components.DocumentItemRow

@Composable
fun DocumentsRoute(modifier: Modifier = Modifier, viewModel: DocumentsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DocumentsScreen(
        uiState = uiState,
        onToggleItem = viewModel::onToggleItem,
        onVisitMoFA = viewModel::openMoFAPortal,
        onRetry = {},
        modifier = modifier,
    )
}

@Composable
fun DocumentsScreen(
    uiState: DocumentsUiState,
    modifier: Modifier = Modifier,
    onToggleItem: (String) -> Unit = {},
    onVisitMoFA: () -> Unit = {},
    onRetry: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(id = R.string.nav_documents),
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)
        when (uiState) {
            is DocumentsUiState.Loading -> AppLoadingWheel(modifier = contentModifier)

            is DocumentsUiState.Empty -> AppEmptyContent(modifier = contentModifier)

            is DocumentsUiState.Error -> AppErrorContent(
                message = uiState.message.asString(),
                onRetry = onRetry,
                modifier = contentModifier,
            )

            is DocumentsUiState.Content -> DocumentsContent(
                data = uiState.data,
                onToggleItem = onToggleItem,
                onVisitMoFA = onVisitMoFA,
                modifier = contentModifier,
            )

            is DocumentsUiState.OfflineStale -> Column(modifier = contentModifier) {
                OfflineBanner(lastUpdated = "2025-01-15")
                DocumentsContent(
                    data = uiState.data,
                    onToggleItem = onToggleItem,
                    onVisitMoFA = onVisitMoFA,
                )
            }
        }
    }
}

@Composable
private fun DocumentsContent(data: DocumentsData, onToggleItem: (String) -> Unit, onVisitMoFA: () -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            ProgressBarWithLabel(
                progress = data.progressPercentage / 100f,
                label = stringResource(id = R.string.doc_progress_title),
            )
        }

        items(
            items = data.items,
            key = { it.id },
        ) { item ->
            DocumentItemRow(
                item = item,
                onToggle = { onToggleItem(item.id) },
            )
        }

        item {
            AttestationStepsSection(
                onVisitMoFA = onVisitMoFA,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@ThemePreviews
@Composable
fun DocumentsScreenPreview() {
    StudyAbroadTheme {
        DocumentsScreen(
            uiState = DocumentsUiState.Content(
                data = DocumentsData(
                    items = PreviewData.sampleChecklistItems,
                    completedCount = 2,
                    totalCount = 4,
                    progressPercentage = 50,
                ),
            ),
        )
    }
}
