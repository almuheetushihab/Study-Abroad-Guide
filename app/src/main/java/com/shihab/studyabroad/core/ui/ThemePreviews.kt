package com.shihab.studyabroad.core.ui

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Light Mode",
    group = "UI Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Preview(
    name = "Dark Mode",
    group = "UI Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
annotation class ThemePreviews
