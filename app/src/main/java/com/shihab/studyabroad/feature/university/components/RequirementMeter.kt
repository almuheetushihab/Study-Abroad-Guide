package com.shihab.studyabroad.feature.university.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.domain.model.RequirementStatus
import com.shihab.studyabroad.domain.usecase.RequirementComparisonResult

/**
 * Requirement vs Your Profile Meter (FR-UNI-003).
 * Shows OK, Low or Unknown status. Missing values MUST display Unknown.
 */
@Composable
fun RequirementMeter(
    comparison: RequirementComparisonResult,
    minCgpa: Double?,
    minIelts: Double?,
    minExp: Int?,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.university_req_vs_profile),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(12.dp))

            RequirementRow(
                label = stringResource(R.string.req_cgpa),
                value = minCgpa?.toString() ?: stringResource(R.string.status_unknown),
                status = comparison.cgpaStatus,
            )
            Spacer(modifier = Modifier.height(8.dp))

            RequirementRow(
                label = stringResource(R.string.req_test),
                value = minIelts?.toString() ?: stringResource(R.string.status_unknown),
                status = comparison.testStatus,
            )
            Spacer(modifier = Modifier.height(8.dp))

            RequirementRow(
                label = stringResource(R.string.req_exp),
                value = if (minExp != null) "$minExp yrs" else stringResource(R.string.status_unknown),
                status = comparison.expStatus,
            )
        }
    }
}

@Composable
private fun RequirementRow(label: String, value: String, status: RequirementStatus) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Required: $value", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
        }

        RequirementStatusChip(status = status)
    }
}

@Composable
private fun RequirementStatusChip(status: RequirementStatus) {
    val (containerColor, contentColor, textRes) = when (status) {
        RequirementStatus.OK -> Triple(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
            R.string.status_ok,
        )

        RequirementStatus.LOW -> Triple(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
            R.string.status_low,
        )

        RequirementStatus.UNKNOWN -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            R.string.status_unknown,
        )
    }

    Surface(
        color = containerColor,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.heightIn(min = 32.dp),
    ) {
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        )
    }
}

@ThemePreviews
@Composable
fun RequirementMeterPreview() {
    StudyAbroadTheme {
        RequirementMeter(
            comparison = RequirementComparisonResult(
                cgpaStatus = RequirementStatus.OK,
                testStatus = RequirementStatus.LOW,
                expStatus = RequirementStatus.UNKNOWN,
            ),
            minCgpa = 3.2,
            minIelts = 7.5,
            minExp = null,
        )
    }
}
