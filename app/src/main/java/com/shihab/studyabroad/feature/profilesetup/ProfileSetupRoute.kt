package com.shihab.studyabroad.feature.profilesetup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileSetupRoute(isEditMode: Boolean, onProfileSetupComplete: () -> Unit, viewModel: ProfileSetupViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(isEditMode) {
        viewModel.initEditMode(isEditMode)
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onProfileSetupComplete()
        }
    }

    ProfileSetupScreen(
        uiState = uiState,
        onLevelChanged = viewModel::onLevelChanged,
        onSubjectChanged = viewModel::onSubjectChanged,
        onCgpaChanged = viewModel::onCgpaChanged,
        onCgpaScaleChanged = viewModel::onCgpaScaleChanged,
        onTestTypeChanged = viewModel::onTestTypeChanged,
        onTestScoreChanged = viewModel::onTestScoreChanged,
        onExperienceYearsChanged = viewModel::onExperienceYearsChanged,
        onAgeChanged = viewModel::onAgeChanged,
        onBudgetRangeChanged = viewModel::onBudgetRangeChanged,
        onGoalChanged = viewModel::onGoalChanged,
        onCountryToggled = viewModel::onCountryToggled,
        onNextStep = viewModel::onNextStep,
        onPreviousStep = viewModel::onPreviousStep,
    )
}
