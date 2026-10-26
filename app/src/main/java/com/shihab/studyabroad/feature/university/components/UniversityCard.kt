package com.shihab.studyabroad.feature.university.components

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.PreviewData
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.domain.model.University

@Composable
fun UniversityCard(university: University, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = university.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                )
                if (university.worldRank != null) {
                    SuggestionChip(
                        onClick = {},
                        label = { Text(text = stringResource(R.string.label_world_rank, university.worldRank)) },
                        modifier = Modifier.heightIn(min = 32.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (!university.tuitionFeeText.isNullOrBlank()) {
                Text(
                    text = "${stringResource(R.string.label_tuition)}: ${university.tuitionFeeText}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            if (university.programs.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Programs: ${university.programs.joinToString(", ")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun UniversityCardPreview() {
    StudyAbroadTheme {
        UniversityCard(
            university = PreviewData.sampleUniversities.first(),
            onClick = {},
        )
    }
}
