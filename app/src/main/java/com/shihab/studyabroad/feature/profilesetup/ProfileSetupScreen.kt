package com.shihab.studyabroad.feature.profilesetup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.shihab.studyabroad.core.designsystem.component.SecondaryButton
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.domain.model.Country
import com.shihab.studyabroad.domain.model.CountryTier
import com.shihab.studyabroad.domain.model.FundingGoal
import com.shihab.studyabroad.domain.model.TestType
import com.shihab.studyabroad.feature.profilesetup.components.ProfileStepIndicator
import com.shihab.studyabroad.feature.profilesetup.components.Step1Academic
import com.shihab.studyabroad.feature.profilesetup.components.Step2Scores
import com.shihab.studyabroad.feature.profilesetup.components.Step3ExperienceAge
import com.shihab.studyabroad.feature.profilesetup.components.Step4BudgetCountries
import kotlinx.datetime.LocalDate

@Composable
fun ProfileSetupScreen(
    uiState: ProfileSetupUiState,
    onLevelChanged: (String) -> Unit,
    onSubjectChanged: (String) -> Unit,
    onCgpaChanged: (String) -> Unit,
    onCgpaScaleChanged: (Double) -> Unit,
    onTestTypeChanged: (TestType) -> Unit,
    onTestScoreChanged: (String) -> Unit,
    onExperienceYearsChanged: (String) -> Unit,
    onAgeChanged: (String) -> Unit,
    onBudgetRangeChanged: (String) -> Unit,
    onGoalChanged: (FundingGoal) -> Unit,
    onCountryToggled: (String) -> Unit,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
) {
    when (uiState) {
        is ProfileSetupUiState.Loading -> AppLoadingWheel(modifier = modifier)

        is ProfileSetupUiState.Error -> AppErrorContent(
            message = uiState.message.asString(),
            onRetry = onRetry,
            modifier = modifier,
        )

        is ProfileSetupUiState.Content -> {
            ProfileSetupContent(
                uiState = uiState,
                onLevelChanged = onLevelChanged,
                onSubjectChanged = onSubjectChanged,
                onCgpaChanged = onCgpaChanged,
                onCgpaScaleChanged = onCgpaScaleChanged,
                onTestTypeChanged = onTestTypeChanged,
                onTestScoreChanged = onTestScoreChanged,
                onExperienceYearsChanged = onExperienceYearsChanged,
                onAgeChanged = onAgeChanged,
                onBudgetRangeChanged = onBudgetRangeChanged,
                onGoalChanged = onGoalChanged,
                onCountryToggled = onCountryToggled,
                onNextStep = onNextStep,
                onPreviousStep = onPreviousStep,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun ProfileSetupContent(
    uiState: ProfileSetupUiState.Content,
    onLevelChanged: (String) -> Unit,
    onSubjectChanged: (String) -> Unit,
    onCgpaChanged: (String) -> Unit,
    onCgpaScaleChanged: (Double) -> Unit,
    onTestTypeChanged: (TestType) -> Unit,
    onTestScoreChanged: (String) -> Unit,
    onExperienceYearsChanged: (String) -> Unit,
    onAgeChanged: (String) -> Unit,
    onBudgetRangeChanged: (String) -> Unit,
    onGoalChanged: (FundingGoal) -> Unit,
    onCountryToggled: (String) -> Unit,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (uiState.isEditMode) {
                    stringResource(id = R.string.profile_edit_title)
                } else {
                    stringResource(id = R.string.profile_setup_title)
                },
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileStepIndicator(
                currentStep = uiState.currentStep,
                totalSteps = uiState.totalSteps,
            )

            Spacer(modifier = Modifier.height(24.dp))

            when (uiState.currentStep) {
                1 -> Step1Academic(
                    formState = uiState.formState,
                    validationResult = uiState.validationResult,
                    onLevelChanged = onLevelChanged,
                    onSubjectChanged = onSubjectChanged,
                )

                2 -> Step2Scores(
                    formState = uiState.formState,
                    validationResult = uiState.validationResult,
                    onCgpaChanged = onCgpaChanged,
                    onCgpaScaleChanged = onCgpaScaleChanged,
                    onTestTypeChanged = onTestTypeChanged,
                    onTestScoreChanged = onTestScoreChanged,
                )

                3 -> Step3ExperienceAge(
                    formState = uiState.formState,
                    validationResult = uiState.validationResult,
                    onExperienceYearsChanged = onExperienceYearsChanged,
                    onAgeChanged = onAgeChanged,
                )

                4 -> Step4BudgetCountries(
                    formState = uiState.formState,
                    validationResult = uiState.validationResult,
                    availableCountries = uiState.availableCountries,
                    onBudgetRangeChanged = onBudgetRangeChanged,
                    onGoalChanged = onGoalChanged,
                    onCountryToggled = onCountryToggled,
                )
            }
        }

        // Navigation controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            if (uiState.currentStep > 1) {
                SecondaryButton(
                    text = stringResource(id = R.string.btn_previous),
                    onClick = onPreviousStep,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(16.dp))
            }

            PrimaryButton(
                text = if (uiState.currentStep == uiState.totalSteps) {
                    stringResource(id = R.string.btn_save_profile)
                } else {
                    stringResource(id = R.string.btn_next)
                },
                onClick = onNextStep,
                enabled = !uiState.isLoading,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@ThemePreviews
@Composable
fun ProfileSetupScreenStep1Preview() {
    StudyAbroadTheme {
        ProfileSetupScreen(
            uiState = ProfileSetupUiState.Content(currentStep = 1),
            onLevelChanged = {},
            onSubjectChanged = {},
            onCgpaChanged = {},
            onCgpaScaleChanged = {},
            onTestTypeChanged = {},
            onTestScoreChanged = {},
            onExperienceYearsChanged = {},
            onAgeChanged = {},
            onBudgetRangeChanged = {},
            onGoalChanged = {},
            onCountryToggled = {},
            onNextStep = {},
            onPreviousStep = {},
        )
    }
}

@ThemePreviews
@Composable
fun ProfileSetupScreenStep2Preview() {
    StudyAbroadTheme {
        ProfileSetupScreen(
            uiState = ProfileSetupUiState.Content(currentStep = 2),
            onLevelChanged = {},
            onSubjectChanged = {},
            onCgpaChanged = {},
            onCgpaScaleChanged = {},
            onTestTypeChanged = {},
            onTestScoreChanged = {},
            onExperienceYearsChanged = {},
            onAgeChanged = {},
            onBudgetRangeChanged = {},
            onGoalChanged = {},
            onCountryToggled = {},
            onNextStep = {},
            onPreviousStep = {},
        )
    }
}

@ThemePreviews
@Composable
fun ProfileSetupScreenStep3Preview() {
    StudyAbroadTheme {
        ProfileSetupScreen(
            uiState = ProfileSetupUiState.Content(currentStep = 3),
            onLevelChanged = {},
            onSubjectChanged = {},
            onCgpaChanged = {},
            onCgpaScaleChanged = {},
            onTestTypeChanged = {},
            onTestScoreChanged = {},
            onExperienceYearsChanged = {},
            onAgeChanged = {},
            onBudgetRangeChanged = {},
            onGoalChanged = {},
            onCountryToggled = {},
            onNextStep = {},
            onPreviousStep = {},
        )
    }
}

@ThemePreviews
@Composable
fun ProfileSetupScreenStep4Preview() {
    StudyAbroadTheme {
        ProfileSetupScreen(
            uiState = ProfileSetupUiState.Content(
                currentStep = 4,
                availableCountries = listOf(
                    Country(
                        id = "de",
                        name = "Germany",
                        tier = CountryTier.SCHOLARSHIP,
                        tuitionRangeText = "Free",
                        livingRangeText = "EUR 800-1000",
                        workRuleText = "120 days/year",
                        postStudyText = "18 months",
                        advisory = null,
                        sourceUrl = "https://daad.de",
                        verifiedOn = LocalDate(2026, 10, 1),
                    ),
                    Country(
                        id = "hu",
                        name = "Hungary",
                        tier = CountryTier.SCHOLARSHIP,
                        tuitionRangeText = "Free with Stipendium",
                        livingRangeText = "EUR 400-600",
                        workRuleText = "30h/week",
                        postStudyText = "9 months",
                        advisory = null,
                        sourceUrl = "https://stipendiumhungaricum.hu",
                        verifiedOn = LocalDate(2026, 10, 1),
                    ),
                ),
            ),
            onLevelChanged = {},
            onSubjectChanged = {},
            onCgpaChanged = {},
            onCgpaScaleChanged = {},
            onTestTypeChanged = {},
            onTestScoreChanged = {},
            onExperienceYearsChanged = {},
            onAgeChanged = {},
            onBudgetRangeChanged = {},
            onGoalChanged = {},
            onCountryToggled = {},
            onNextStep = {},
            onPreviousStep = {},
        )
    }
}
