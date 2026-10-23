package com.shihab.studyabroad.feature.scholarship

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.core.common.UrlOpener
import com.shihab.studyabroad.domain.model.ScholarshipStatus
import com.shihab.studyabroad.domain.repository.ScholarshipRepository
import com.shihab.studyabroad.domain.usecase.GetScholarshipStatusUseCase
import com.shihab.studyabroad.domain.usecase.IsRecordStaleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ScholarshipDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    scholarshipRepository: ScholarshipRepository,
    private val getScholarshipStatusUseCase: GetScholarshipStatusUseCase,
    private val isRecordStaleUseCase: IsRecordStaleUseCase,
    val urlOpener: UrlOpener,
) : ViewModel() {

    private val scholarshipId: String = checkNotNull(savedStateHandle["id"])
    private val selectedReminderDays = MutableStateFlow<Int?>(null)

    val uiState: StateFlow<ScholarshipDetailUiState> = combine(
        scholarshipRepository.getScholarshipById(scholarshipId),
        selectedReminderDays,
    ) { scholarship, reminderDays ->
        if (scholarship == null) {
            ScholarshipDetailUiState.Empty
        } else {
            val status = getScholarshipStatusUseCase(scholarship)
            val isStale = isRecordStaleUseCase(scholarship.verifiedOn)
            val expectedNextCycle = if (status == ScholarshipStatus.CLOSED) {
                "Expected: Aug ${scholarship.deadline.year + 1}"
            } else {
                null
            }

            val confirmationMessage = if (reminderDays != null) {
                "Reminder set for $reminderDays days before deadline."
            } else {
                null
            }

            ScholarshipDetailUiState.Content(
                ScholarshipDetailData(
                    scholarship = scholarship,
                    status = status,
                    expectedNextCycle = expectedNextCycle,
                    isStale = isStale,
                    reminderDays = reminderDays,
                    reminderConfirmationMessage = confirmationMessage,
                ),
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ScholarshipDetailUiState.Loading,
    )

    fun onSetReminderDays(days: Int) {
        selectedReminderDays.value = days
    }

    fun openApplyUrl(url: String) {
        urlOpener.openUrl(url)
    }
}
