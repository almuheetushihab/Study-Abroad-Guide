package com.shihab.studyabroad.feature.scholarship

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScholarshipScreen(modifier: Modifier = Modifier, onNavigateToScholarshipDetail: (String) -> Unit = {}) {
    ScholarshipListRoute(
        onNavigateToScholarshipDetail = onNavigateToScholarshipDetail,
    )
}
