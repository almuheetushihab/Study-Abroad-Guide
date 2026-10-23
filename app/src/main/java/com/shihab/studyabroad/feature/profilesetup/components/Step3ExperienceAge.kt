package com.shihab.studyabroad.feature.profilesetup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.designsystem.component.AppTextField
import com.shihab.studyabroad.domain.usecase.ProfileValidationResult
import com.shihab.studyabroad.feature.profilesetup.ProfileFormState

@Composable
fun Step3ExperienceAge(
    formState: ProfileFormState,
    validationResult: ProfileValidationResult,
    onExperienceYearsChanged: (String) -> Unit,
    onAgeChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        AppTextField(
            value = formState.experienceYears,
            onValueChange = onExperienceYearsChanged,
            label = stringResource(id = R.string.label_experience_years),
            isError = !validationResult.isExperienceValid,
            supportingText = validationResult.experienceError?.asString(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppTextField(
            value = formState.age,
            onValueChange = onAgeChanged,
            label = stringResource(id = R.string.label_age),
            isError = !validationResult.isAgeValid,
            supportingText = validationResult.ageError?.asString(),
        )
    }
}
