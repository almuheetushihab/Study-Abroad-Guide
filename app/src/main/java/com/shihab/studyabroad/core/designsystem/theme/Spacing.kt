package com.shihab.studyabroad.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Design system spacing definitions on a 4dp grid:
 * xxs: 2dp, xs: 4dp, sm: 8dp, md: 12dp, lg: 16dp, xl: 24dp, xxl: 32dp, xxxl: 48dp.
 */
data class Spacing(
    val xxs: Dp = 2.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 24.dp,
    val xxl: Dp = 32.dp,
    val xxxl: Dp = 48.dp,
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }

val Spacing.current: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current
