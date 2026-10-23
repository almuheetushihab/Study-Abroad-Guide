package com.shihab.studyabroad.feature.profilesetup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.designsystem.component.AppTextField
import com.shihab.studyabroad.domain.usecase.ProfileValidationResult
import com.shihab.studyabroad.feature.profilesetup.ProfileFormState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Step1Academic(
    formState: ProfileFormState,
    validationResult: ProfileValidationResult,
    onLevelChanged: (String) -> Unit,
    onSubjectChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val levels = listOf("Bachelor", "Master", "PhD", "Diploma")

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.label_degree_level),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            levels.forEach { levelOption ->
                val isSelected = formState.level.equals(levelOption, ignoreCase = true)
                FilterChip(
                    selected = isSelected,
                    onClick = { onLevelChanged(levelOption) },
                    label = { Text(text = levelOption) },
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                )
            }
        }

        if (!validationResult.isLevelValid && validationResult.levelError != null) {
            Text(
                text = validationResult.levelError.asString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AppTextField(
            value = formState.subject,
            onValueChange = onSubjectChanged,
            label = stringResource(id = R.string.label_subject),
            isError = !validationResult.isSubjectValid,
            supportingText = validationResult.subjectError?.asString(),
        )
    }
}
