package com.shihab.studyabroad.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.studyabroad.domain.usecase.GetStartDestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(getStartDestinationUseCase: GetStartDestinationUseCase) : ViewModel() {

    val uiState: StateFlow<SplashUiState> = getStartDestinationUseCase()
        .map { destination -> SplashUiState.Content(destination) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SplashUiState.Loading,
        )
}
