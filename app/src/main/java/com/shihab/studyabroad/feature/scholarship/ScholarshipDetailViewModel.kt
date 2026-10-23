package com.shihab.studyabroad.feature.scholarship

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.core.common.UrlOpener
import com.shihab.studyabroad.domain.model.ReportRefType
import com.shihab.studyabroad.domain.model.SavedType
import com.shihab.studyabroad.domain.model.ScholarshipStatus
import com.shihab.studyabroad.domain.repository.SavedRepository
import com.shihab.studyabroad.domain.repository.ScholarshipRepository
import com.shihab.studyabroad.domain.usecase.GetScholarshipStatusUseCase
import com.shihab.studyabroad.domain.usecase.IsRecordStaleUseCase
import com.shihab.studyabroad.domain.usecase.SubmitReportUseCase
import com.shihab.studyabroad.domain.usecase.ToggleSavedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScholarshipDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    scholarshipRepository: ScholarshipRepository,
    savedRepository: SavedRepository,
    private val toggleSavedUseCase: ToggleSavedUseCase,
    private val getScholarshipStatusUseCase: GetScholarshipStatusUseCase,
    private val isRecordStaleUseCase: IsRecordStaleUseCase,
    private val submitReportUseCase: SubmitReportUseCase,
    val urlOpener: UrlOpener,
) : ViewModel() {

    private val scholarshipId: String = checkNotNull(savedStateHandle["id"])
    private val selectedReminderDays = MutableStateFlow<Int?>(null)

    val uiState: StateFlow<ScholarshipDetailUiState> = combine(
        scholarshipRepository.getScholarshipById(scholarshipId),
        selectedReminderDays,
        savedRepository.isSaved(SavedType.SCHOLARSHIP, scholarshipId),
    ) { scholarship, reminderDays, isSaved ->
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
                    isSaved = isSaved,
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

    fun onToggleSave() {
        viewModelScope.launch {
            toggleSavedUseCase(SavedType.SCHOLARSHIP, scholarshipId)
        }
    }

    fun submitReport(message: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = submitReportUseCase(ReportRefType.SCHOLARSHIP, scholarshipId, message)
            onComplete(result is AppResult.Success)
        }
    }

    fun openApplyUrl(url: String) {
        urlOpener.openUrl(url)
    }
}
