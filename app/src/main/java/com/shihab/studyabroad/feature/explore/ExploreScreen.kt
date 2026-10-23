package com.shihab.studyabroad.feature.explore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.R

@Composable
fun ExploreScreen(
    onNavigateToCountries: () -> Unit,
    onNavigateToScholarships: () -> Unit,
    onNavigateToUniversities: () -> Unit,
    onNavigateToCountryComparison: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.explore_hub_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(id = R.string.explore_hub_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExploreCard(
            title = stringResource(id = R.string.nav_countries),
            description = stringResource(id = R.string.explore_countries_desc),
            onClick = onNavigateToCountries,
        )

        ExploreCard(
            title = stringResource(id = R.string.country_comparison_title),
            description = stringResource(id = R.string.country_compare_select_hint),
            onClick = onNavigateToCountryComparison,
        )

        ExploreCard(
            title = stringResource(id = R.string.nav_scholarships),
            description = stringResource(id = R.string.explore_scholarships_desc),
            onClick = onNavigateToScholarships,
        )

        ExploreCard(
            title = stringResource(id = R.string.nav_universities),
            description = stringResource(id = R.string.explore_universities_desc),
            onClick = onNavigateToUniversities,
        )
    }
}

@Composable
private fun ExploreCard(title: String, description: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
