package com.shihab.studyabroad.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shihab.studyabroad.navigation.Route

@Composable
fun SplashRoute(onNavigateToDestination: (Route) -> Unit, viewModel: SplashViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is SplashUiState.Content) {
            onNavigateToDestination((uiState as SplashUiState.Content).startDestination)
        }
    }

    SplashScreen(uiState = uiState)
}
