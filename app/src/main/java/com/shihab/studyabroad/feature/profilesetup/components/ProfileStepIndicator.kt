package com.shihab.studyabroad.feature.profilesetup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.R

@Composable
fun ProfileStepIndicator(currentStep: Int, totalSteps: Int, modifier: Modifier = Modifier) {
    val stepNameRes = when (currentStep) {
        1 -> R.string.step_1_name
        2 -> R.string.step_2_name
        3 -> R.string.step_3_name
        else -> R.string.step_4_name
    }

    val progress = currentStep.toFloat() / totalSteps.toFloat()

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.step_indicator, currentStep, totalSteps, stringResource(id = stepNameRes)),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}
