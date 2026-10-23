package com.shihab.studyabroad.feature.trackselect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TrackSelectRoute(onTrackSelectComplete: () -> Unit, viewModel: TrackSelectViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onTrackSelectComplete()
        }
    }

    TrackSelectScreen(
        uiState = uiState,
        onSelectTrack = viewModel::onSelectTrack,
        onContinue = viewModel::onContinue,
    )
}
