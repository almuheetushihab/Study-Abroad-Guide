package com.yourname.studyabroad.core.common

/**
 * Generic sealed class representing operation results across the application.
 */
sealed interface AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>

    data class Error(val throwable: Throwable, val message: String? = throwable.message) : AppResult<Nothing>

    data object Loading : AppResult<Nothing>
}
