package com.shihab.studyabroad.feature.splash

import com.shihab.studyabroad.navigation.Route

sealed interface SplashUiState {
    data object Loading : SplashUiState
    data class Content(val startDestination: Route) : SplashUiState
}
