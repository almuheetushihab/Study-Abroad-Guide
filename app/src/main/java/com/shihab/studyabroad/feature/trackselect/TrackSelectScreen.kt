package com.shihab.studyabroad.feature.trackselect

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.designsystem.component.AppErrorContent
import com.shihab.studyabroad.core.designsystem.component.AppLoadingWheel
import com.shihab.studyabroad.core.designsystem.component.PrimaryButton
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.domain.model.Track
import com.shihab.studyabroad.feature.trackselect.components.TrackOptionCard

@Composable
fun TrackSelectScreen(
    uiState: TrackSelectUiState,
    onSelectTrack: (Track) -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
) {
    when (uiState) {
        is TrackSelectUiState.Loading -> AppLoadingWheel(modifier = modifier)

        is TrackSelectUiState.Error -> AppErrorContent(
            message = uiState.message.asString(),
            onRetry = onRetry,
            modifier = modifier,
        )

        is TrackSelectUiState.Content -> {
            TrackSelectContent(
                uiState = uiState,
                onSelectTrack = onSelectTrack,
                onContinue = onContinue,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun TrackSelectContent(
    uiState: TrackSelectUiState.Content,
    onSelectTrack: (Track) -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.track_select_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.track_select_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Track.entries.forEach { track ->
            TrackOptionCard(
                track = track,
                isSelected = uiState.selectedTrack == track,
                onSelect = { onSelectTrack(track) },
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = stringResource(id = R.string.btn_continue),
            onClick = onContinue,
            enabled = !uiState.isLoading && uiState.selectedTrack != null,
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@ThemePreviews
@Composable
fun TrackSelectScreenPreviewUnselected() {
    StudyAbroadTheme {
        TrackSelectScreen(
            uiState = TrackSelectUiState.Content(selectedTrack = null),
            onSelectTrack = {},
            onContinue = {},
        )
    }
}

@ThemePreviews
@Composable
fun TrackSelectScreenPreviewSelected() {
    StudyAbroadTheme {
        TrackSelectScreen(
            uiState = TrackSelectUiState.Content(selectedTrack = Track.GRADUATE),
            onSelectTrack = {},
            onContinue = {},
        )
    }
}
