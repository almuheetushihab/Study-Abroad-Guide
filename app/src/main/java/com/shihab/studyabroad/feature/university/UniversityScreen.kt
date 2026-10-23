package com.shihab.studyabroad.feature.university

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun UniversityScreen(modifier: Modifier = Modifier, onNavigateToUniversityDetail: (String) -> Unit = {}) {
    UniversityListRoute(
        onNavigateToUniversityDetail = onNavigateToUniversityDetail,
    )
}
