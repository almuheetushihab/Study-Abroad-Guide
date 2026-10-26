package com.shihab.studyabroad.feature.country.components

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
import com.shihab.studyabroad.core.designsystem.component.WarningBanner
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.PreviewData
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.domain.model.Country
import com.shihab.studyabroad.domain.model.CountryTier

@Composable
fun CountryCard(country: Country, onClick: () -> Unit, modifier: Modifier = Modifier) {
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
                    text = country.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                val tierText = when (country.tier) {
                    CountryTier.SCHOLARSHIP -> stringResource(R.string.country_tier_scholarship)
                    CountryTier.PR_WORK -> stringResource(R.string.country_tier_pr_work)
                }
                SuggestionChip(
                    onClick = {},
                    label = { Text(text = tierText, style = MaterialTheme.typography.labelSmall) },
                    modifier = Modifier.heightIn(min = 32.dp),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${stringResource(R.string.label_tuition)}: ${country.tuitionRangeText}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "${stringResource(R.string.label_living_cost)}: ${country.livingRangeText}",
                style = MaterialTheme.typography.bodyMedium,
            )

            val advisoryText = country.advisory
            if (!advisoryText.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                WarningBanner(message = advisoryText)
            }
        }
    }
}

@ThemePreviews
@Composable
fun CountryCardPreview() {
    StudyAbroadTheme {
        CountryCard(
            country = PreviewData.sampleCountries.first(),
            onClick = {},
        )
    }
}
