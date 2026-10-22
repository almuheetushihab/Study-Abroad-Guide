package com.shihab.studyabroad.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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

@Composable
fun HomeRoute(viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(uiState = uiState)
}

@Composable
fun HomeScreen(uiState: HomeUiState, modifier: Modifier = Modifier) {
    when (uiState) {
        is HomeUiState.Loading -> AppLoadingWheel(modifier = modifier)
        is HomeUiState.Empty -> AppEmptyContent(modifier = modifier)
        is HomeUiState.Error -> AppErrorContent(
            message = uiState.message.asString(),
            onRetry = {},
            modifier = modifier,
        )
        is HomeUiState.Content -> HomeContent(data = uiState.data, modifier = modifier)
        is HomeUiState.OfflineStale -> HomeContent(data = uiState.data, modifier = modifier)
    }
}

@Composable
private fun HomeContent(data: HomeData, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Featured Scholarships: ${data.featuredScholarships.size}",
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = "Top Countries: ${data.topCountries.size}",
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = stringResource(id = R.string.sample_data_disclaimer),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}
