package com.shihab.studyabroad.feature.country

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CountryScreen(
    modifier: Modifier = Modifier,
    onNavigateToCountryDetail: (String) -> Unit = {},
    onNavigateToCountryComparison: () -> Unit = {},
) {
    CountryListRoute(
        onNavigateToCountryDetail = onNavigateToCountryDetail,
        onNavigateToCountryComparison = onNavigateToCountryComparison,
    )
}
