package com.shihab.studyabroad.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.designsystem.theme.LocalStatusColors
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.domain.model.ScholarshipStatus

/**
 * Status chip indicating Open, Opening soon, or Closed status.
 * Combines color, icon, and text (never relies on color alone).
 */
@Composable
fun StatusChip(status: ScholarshipStatus, modifier: Modifier = Modifier) {
    val statusColors = LocalStatusColors.current
    val (containerColor, contentColor, vector, label) = when (status) {
        ScholarshipStatus.OPEN -> Quadruple(
            statusColors.openContainerColor,
            statusColors.onOpenContainerColor,
            Icons.Default.CheckCircle,
            stringResource(R.string.status_open),
        )

        ScholarshipStatus.OPENING_SOON -> Quadruple(
            statusColors.openingSoonContainerColor,
            statusColors.onOpeningSoonContainerColor,
            Icons.Default.Schedule,
            stringResource(R.string.status_opening_soon),
        )

        ScholarshipStatus.CLOSED -> Quadruple(
            statusColors.closedContainerColor,
            statusColors.onClosedContainerColor,
            Icons.Default.Cancel,
            stringResource(R.string.status_closed),
        )
    }

    Surface(
        modifier = modifier
            .heightIn(min = 48.dp)
            .semantics { contentDescription = label },
        color = containerColor,
        shape = MaterialTheme.shapes.small,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = vector,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor,
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@ThemePreviews
@Composable
fun StatusChipPreview() {
    StudyAbroadTheme {
        Row {
            StatusChip(status = ScholarshipStatus.OPEN)
            StatusChip(status = ScholarshipStatus.OPENING_SOON)
            StatusChip(status = ScholarshipStatus.CLOSED)
        }
    }
}
