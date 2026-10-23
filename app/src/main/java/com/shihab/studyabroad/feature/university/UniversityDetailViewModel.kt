package com.shihab.studyabroad.feature.university

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.core.common.UrlOpener
import com.shihab.studyabroad.domain.repository.UniversityRepository
import com.shihab.studyabroad.domain.repository.UserProfileRepository
import com.shihab.studyabroad.domain.usecase.CompareRequirementUseCase
import com.shihab.studyabroad.domain.usecase.IsRecordStaleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class UniversityDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    universityRepository: UniversityRepository,
    userProfileRepository: UserProfileRepository,
    private val compareRequirementUseCase: CompareRequirementUseCase,
    private val isRecordStaleUseCase: IsRecordStaleUseCase,
    val urlOpener: UrlOpener,
) : ViewModel() {

    private val universityId: String = checkNotNull(savedStateHandle["id"])

    val uiState: StateFlow<UniversityDetailUiState> = combine(
        universityRepository.getUniversityById(universityId),
        userProfileRepository.getUserProfile(),
    ) { university, profile ->
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
                ),
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UniversityDetailUiState.Loading,
    )

    fun openWebsite(url: String) {
        urlOpener.openUrl(url)
    }
}
