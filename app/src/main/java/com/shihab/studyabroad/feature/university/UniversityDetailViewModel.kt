package com.shihab.studyabroad.feature.university

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.core.common.UrlOpener
import com.shihab.studyabroad.domain.model.ReportRefType
import com.shihab.studyabroad.domain.model.SavedType
import com.shihab.studyabroad.domain.repository.SavedRepository
import com.shihab.studyabroad.domain.repository.UniversityRepository
import com.shihab.studyabroad.domain.repository.UserProfileRepository
import com.shihab.studyabroad.domain.usecase.CompareRequirementUseCase
import com.shihab.studyabroad.domain.usecase.IsRecordStaleUseCase
import com.shihab.studyabroad.domain.usecase.SubmitReportUseCase
import com.shihab.studyabroad.domain.usecase.ToggleSavedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UniversityDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    universityRepository: UniversityRepository,
    userProfileRepository: UserProfileRepository,
    savedRepository: SavedRepository,
    private val toggleSavedUseCase: ToggleSavedUseCase,
    private val compareRequirementUseCase: CompareRequirementUseCase,
    private val isRecordStaleUseCase: IsRecordStaleUseCase,
    private val submitReportUseCase: SubmitReportUseCase,
    val urlOpener: UrlOpener,
) : ViewModel() {

    private val universityId: String = checkNotNull(savedStateHandle["id"])

    val uiState: StateFlow<UniversityDetailUiState> = combine(
        universityRepository.getUniversityById(universityId),
        userProfileRepository.getUserProfile(),
        savedRepository.isSaved(SavedType.UNIVERSITY, universityId),
    ) { university, profile, isSaved ->
        if (university == null) {
            UniversityDetailUiState.Empty
        } else {
            val comparison = compareRequirementUseCase(university, profile)
            val isStale = isRecordStaleUseCase(university.verifiedOn)

            UniversityDetailUiState.Content(
                UniversityDetailData(
                    university = university,
                    requirementComparison = comparison,
                    isStale = isStale,
                    isSaved = isSaved,
                ),
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UniversityDetailUiState.Loading,
    )

    fun onToggleSave() {
        viewModelScope.launch {
            toggleSavedUseCase(SavedType.UNIVERSITY, universityId)
        }
    }

    fun submitReport(message: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = submitReportUseCase(ReportRefType.UNIVERSITY, universityId, message)
            onComplete(result is AppResult.Success)
        }
    }

    fun openWebsite(url: String) {
        urlOpener.openUrl(url)
    }
}
