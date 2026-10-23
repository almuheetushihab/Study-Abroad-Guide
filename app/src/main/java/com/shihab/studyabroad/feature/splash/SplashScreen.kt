package com.shihab.studyabroad.feature.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.feature.splash.components.SplashLogo

@Composable
fun SplashScreen(uiState: SplashUiState, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        SplashLogo()
    }
}

@ThemePreviews
@Composable
fun SplashScreenLoadingPreview() {
    StudyAbroadTheme {
        SplashScreen(uiState = SplashUiState.Loading)
    }
}
