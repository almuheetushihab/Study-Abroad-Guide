package com.shihab.studyabroad.domain.model

sealed interface DeleteAccountResult {
    data object Success : DeleteAccountResult
    data class ReauthenticationRequired(val provider: AuthProvider) : DeleteAccountResult
    data class Error(val throwable: Throwable) : DeleteAccountResult
}
