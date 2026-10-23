package com.shihab.studyabroad.core.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Light Color Palette
val LightPrimary = Color(0xFF1E4FD8)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFFDCE6FF)
val LightOnPrimaryContainer = Color(0xFF0B1F5C)

val LightSecondary = Color(0xFF0F766E)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightSecondaryContainer = Color(0xFFCCFBF1)
val LightOnSecondaryContainer = Color(0xFF042F2B)

val LightTertiary = Color(0xFFB45309)
val LightOnTertiary = Color(0xFFFFFFFF)
val LightTertiaryContainer = Color(0xFFFEF3C7)
val LightOnTertiaryContainer = Color(0xFF451A03)

val LightBackground = Color(0xFFF8FAFC)
val LightOnBackground = Color(0xFF0F172A)
val LightSurface = Color(0xFFFFFFFF)
val LightOnSurface = Color(0xFF0F172A)

val LightSurfaceVariant = Color(0xFFE2E8F0)
val LightOnSurfaceVariant = Color(0xFF475569)
val LightOutline = Color(0xFF94A3B8)

val LightError = Color(0xFFBA1A1A)
val LightOnError = Color(0xFFFFFFFF)

// Dark Color Palette
val DarkPrimary = Color(0xFFAEC6FF)
val DarkOnPrimary = Color(0xFF0B1F5C)
val DarkPrimaryContainer = Color(0xFF1E3A8A)
val DarkOnPrimaryContainer = Color(0xFFDCE6FF)

val DarkSecondary = Color(0xFF5EEAD4)
val DarkOnSecondary = Color(0xFF042F2B)
val DarkSecondaryContainer = Color(0xFF115E59)
val DarkOnSecondaryContainer = Color(0xFFCCFBF1)

val DarkTertiary = Color(0xFFFCD34D)
val DarkOnTertiary = Color(0xFF451A03)
val DarkTertiaryContainer = Color(0xFF78350F)
val DarkOnTertiaryContainer = Color(0xFFFEF3C7)

val DarkBackground = Color(0xFF0F1419)
val DarkOnBackground = Color(0xFFE2E8F0)
val DarkSurface = Color(0xFF161B22)
val DarkOnSurface = Color(0xFFE2E8F0)

val DarkSurfaceVariant = Color(0xFF273140)
val DarkOnSurfaceVariant = Color(0xFFA8B3C4)
val DarkOutline = Color(0xFF64748B)

val DarkError = Color(0xFFFFB4AB)
val DarkOnError = Color(0xFF690005)

/**
 * Custom Status Colors data class for semantic status badges (Open, Opening soon, Closed).
 */
data class StatusColors(
    val openColor: Color,
    val openContainerColor: Color,
    val onOpenContainerColor: Color,
    val openingSoonColor: Color,
    val openingSoonContainerColor: Color,
    val onOpeningSoonContainerColor: Color,
    val closedColor: Color,
    val closedContainerColor: Color,
    val onClosedContainerColor: Color,
)

val LightStatusColors = StatusColors(
    openColor = Color(0xFF15803D),
    openContainerColor = Color(0xFFDCFCE7),
    onOpenContainerColor = Color(0xFF052E16),
    openingSoonColor = Color(0xFFB45309),
    openingSoonContainerColor = Color(0xFFFEF3C7),
    onOpeningSoonContainerColor = Color(0xFF451A03),
    closedColor = Color(0xFF4B5563),
    closedContainerColor = Color(0xFFF3F4F6),
    onClosedContainerColor = Color(0xFF1F2937),
)

val DarkStatusColors = StatusColors(
    openColor = Color(0xFF86EFAC),
    openContainerColor = Color(0xFF052E16),
    onOpenContainerColor = Color(0xFFDCFCE7),
    openingSoonColor = Color(0xFFFCD34D),
    openingSoonContainerColor = Color(0xFF451A03),
    onOpeningSoonContainerColor = Color(0xFFFEF3C7),
    closedColor = Color(0xFF9CA3AF),
    closedContainerColor = Color(0xFF1F2937),
    onClosedContainerColor = Color(0xFFF3F4F6),
)

val LocalStatusColors = staticCompositionLocalOf { LightStatusColors }
