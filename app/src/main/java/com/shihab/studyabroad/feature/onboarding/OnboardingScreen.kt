package com.shihab.studyabroad.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.designsystem.component.PrimaryButton
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.feature.onboarding.components.OnboardingPageContent

@Composable
fun OnboardingScreen(uiState: OnboardingUiState.Content, onNext: () -> Unit, onSkip: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        // Top row with Skip button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            if (uiState.currentPage < uiState.totalPages - 1) {
                TextButton(onClick = onSkip) {
                    Text(
                        text = stringResource(id = R.string.btn_skip),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }

        // Center content for current page
        OnboardingPageContent(
            pageIndex = uiState.currentPage,
            modifier = Modifier.weight(1f),
        )

        // Bottom section: Page indicator and Next/Get Started button
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Page Indicator dots
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp),
            ) {
                repeat(uiState.totalPages) { index ->
                    val isSelected = index == uiState.currentPage
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.outlineVariant
                                },
                            ),
                    )
                }
            }

            // Action button
            val isLastPage = uiState.currentPage == uiState.totalPages - 1
            PrimaryButton(
                text = if (isLastPage) {
                    stringResource(id = R.string.btn_get_started)
                } else {
                    stringResource(id = R.string.btn_next)
                },
                onClick = onNext,
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@ThemePreviews
@Composable
fun OnboardingScreenPreviewPage0() {
    StudyAbroadTheme {
        OnboardingScreen(
            uiState = OnboardingUiState.Content(currentPage = 0),
            onNext = {},
            onSkip = {},
        )
    }
}

@ThemePreviews
@Composable
fun OnboardingScreenPreviewPage2() {
    StudyAbroadTheme {
        OnboardingScreen(
            uiState = OnboardingUiState.Content(currentPage = 2),
            onNext = {},
            onSkip = {},
        )
    }
}
