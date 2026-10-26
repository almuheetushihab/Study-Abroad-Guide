package com.shihab.studyabroad.feature.country.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.PreviewData
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.domain.model.Country

enum class ComparisonSortColumn {
    NAME,
    TIER,
    TUITION,
    LIVING,
}

@Composable
fun CountryComparisonTable(
    selectedCountries: List<Country>,
    sortColumn: ComparisonSortColumn,
    onSortColumnClick: (ComparisonSortColumn) -> Unit,
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
        Column(
            modifier = Modifier
                .padding(16.dp)
                .horizontalScroll(rememberScrollState()),
        ) {
            // Header Row (Sortable by clicking)
            Row(
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TableHeaderCell(
                    title = stringResource(R.string.col_country),
                    isSelected = sortColumn == ComparisonSortColumn.NAME,
                    onClick = { onSortColumnClick(ComparisonSortColumn.NAME) },
                    modifier = Modifier.width(120.dp),
                )
                TableHeaderCell(
                    title = stringResource(R.string.col_tier),
                    isSelected = sortColumn == ComparisonSortColumn.TIER,
                    onClick = { onSortColumnClick(ComparisonSortColumn.TIER) },
                    modifier = Modifier.width(100.dp),
                )
                TableHeaderCell(
                    title = stringResource(R.string.col_tuition),
                    isSelected = sortColumn == ComparisonSortColumn.TUITION,
                    onClick = { onSortColumnClick(ComparisonSortColumn.TUITION) },
                    modifier = Modifier.width(160.dp),
                )
                TableHeaderCell(
                    title = stringResource(R.string.col_living),
                    isSelected = sortColumn == ComparisonSortColumn.LIVING,
                    onClick = { onSortColumnClick(ComparisonSortColumn.LIVING) },
                    modifier = Modifier.width(150.dp),
                )
            }

            HorizontalDivider(color = DividerDefaults.color)

            // Data Rows
            selectedCountries.forEach { country ->
                Row(
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = country.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(120.dp),
                    )
                    Text(
                        text = country.tier.name,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.width(100.dp),
                    )
                    Text(
                        text = country.tuitionRangeText,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.width(160.dp),
                    )
                    Text(
                        text = country.livingRangeText,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.width(150.dp),
                    )
                }
                HorizontalDivider(color = DividerDefaults.color.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
private fun TableHeaderCell(title: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .heightIn(min = 48.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = if (isSelected) "$title ▲" else title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.width(4.dp))
    }
}

@ThemePreviews
@Composable
fun CountryComparisonTablePreview() {
    StudyAbroadTheme {
        CountryComparisonTable(
            selectedCountries = PreviewData.sampleCountries.take(3),
            sortColumn = ComparisonSortColumn.NAME,
            onSortColumnClick = {},
        )
    }
}
