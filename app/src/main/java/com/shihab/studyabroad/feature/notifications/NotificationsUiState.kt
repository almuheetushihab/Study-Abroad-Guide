package com.shihab.studyabroad.feature.notifications

data class NotificationsUiState(
    val deadlineRemindersEnabled: Boolean = true,
    val newScholarshipsEnabled: Boolean = true,
    val appNewsEnabled: Boolean = true,
    val isPermissionGranted: Boolean = true,
)
