package com.shihab.studyabroad.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.BuildConfig
import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.core.common.AuthErrorMapper
import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import com.shihab.studyabroad.domain.usecase.DeleteAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository,
    private val deleteAccountUseCase: DeleteAccountUseCase,
) : ViewModel() {

    private val isDeleteSuccess = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<UiText?>(null)

    val uiState: StateFlow<ProfileUiState> = combine(
        userPreferencesRepository.userProfile,
        isDeleteSuccess,
        errorMessage,
    ) { profile, deleteSuccess, errorMsg ->
        ProfileUiState.Content(
            userProfile = profile,
            appVersion = BuildConfig.VERSION_NAME,
            isDeleteSuccess = deleteSuccess,
            errorMessage = errorMsg,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState.Loading,
    )

    fun onDeleteAccount(onResult: (Boolean, UiText?) -> Unit) {
        viewModelScope.launch {
            errorMessage.value = null
            when (val result = deleteAccountUseCase()) {
                is AppResult.Success -> {
                    isDeleteSuccess.value = true
                    onResult(true, null)
                }
                is AppResult.Error -> {
                    val error = AuthErrorMapper.mapExceptionToUiText(result.throwable)
                    errorMessage.value = error
                    onResult(false, error)
                }
                else -> {}
            }
        }
    }
}
