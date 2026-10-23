package com.shihab.studyabroad.feature.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    val uiState: StateFlow<NotificationsUiState> = combine(
        userPreferencesRepository.deadlineRemindersEnabled,
        userPreferencesRepository.newScholarshipsEnabled,
        userPreferencesRepository.appNewsEnabled,
    ) { deadlineReminders, newScholarships, appNews ->
        NotificationsUiState(
            deadlineRemindersEnabled = deadlineReminders,
            newScholarshipsEnabled = newScholarships,
            appNewsEnabled = appNews,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NotificationsUiState(),
    )

    fun onToggleDeadlineReminders(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setDeadlineRemindersEnabled(enabled)
        }
    }

    fun onToggleNewScholarships(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setNewScholarshipsEnabled(enabled)
        }
    }

    fun onToggleAppNews(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setAppNewsEnabled(enabled)
        }
    }
}
