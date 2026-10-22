package com.yourname.studyabroad.core.ui

/**
 * Standard 5-state sealed interface architecture for all screens in Study Abroad Guide.
 * 1. Loading
 * 2. Content
 * 3. Empty
 * 4. Error
 * 5. OfflineStale
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>

    data class Content<T>(val data: T) : UiState<T>

    data object Empty : UiState<Nothing>

    data class Error(val message: UiText) : UiState<Nothing>

    data class OfflineStale<T>(val data: T, val message: UiText) : UiState<T>
}
