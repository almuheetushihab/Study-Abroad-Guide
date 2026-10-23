package com.shihab.studyabroad.feature.profilesetup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.designsystem.component.AppTextField
import com.shihab.studyabroad.domain.model.TestType
import com.shihab.studyabroad.domain.usecase.ProfileValidationResult
import com.shihab.studyabroad.feature.profilesetup.ProfileFormState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Step2Scores(
    formState: ProfileFormState,
    validationResult: ProfileValidationResult,
    onCgpaChanged: (String) -> Unit,
    onCgpaScaleChanged: (Double) -> Unit,
    onTestTypeChanged: (TestType) -> Unit,
    onTestScoreChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        AppTextField(
            value = formState.cgpa,
            onValueChange = onCgpaChanged,
            label = stringResource(id = R.string.label_cgpa),
            isError = !validationResult.isCgpaValid,
            supportingText = validationResult.cgpaError?.asString(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.label_cgpa_scale),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 16.dp),
            ) {
                RadioButton(
                    selected = formState.cgpaScale == 4.0,
                    onClick = { onCgpaScaleChanged(4.0) },
                )
                Text(
                    text = "4.0",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = formState.cgpaScale == 5.0,
                    onClick = { onCgpaScaleChanged(5.0) },
                )
                Text(
                    text = "5.0",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.label_test_type),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(modifier = Modifier.fillMaxWidth()) {
            TestType.entries.forEach { testType ->
                FilterChip(
                    selected = formState.testType == testType,
                    onClick = { onTestTypeChanged(testType) },
                    label = { Text(text = testType.name) },
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                )
            }
        }

        if (formState.testType != TestType.NONE) {
            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = formState.testScore,
                onValueChange = onTestScoreChanged,
                label = stringResource(id = R.string.label_test_score),
                isError = !validationResult.isTestScoreValid,
                supportingText = validationResult.testScoreError?.asString(),
            )
        }
    }
}
