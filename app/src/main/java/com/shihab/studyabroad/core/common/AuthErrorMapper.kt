package com.shihab.studyabroad.core.common

import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.ui.UiText

object AuthErrorMapper {
    fun mapExceptionToUiText(throwable: Throwable?): UiText {
        if (throwable == null) return UiText.StringResource(R.string.error_auth_generic)

        val name = throwable.javaClass.simpleName
        val message = throwable.message ?: ""

        return when {
            name.contains("FirebaseAuthInvalidCredentialsException") || message.contains("invalid", ignoreCase = true) ->
                UiText.StringResource(R.string.error_auth_invalid_code)
            name.contains("FirebaseAuthRecentLoginRequiredException") || message.contains("recent", ignoreCase = true) ->
                UiText.StringResource(R.string.error_auth_requires_recent_login)
            name.contains("FirebaseTooManyRequestsException") || message.contains("too many", ignoreCase = true) ->
                UiText.StringResource(R.string.error_auth_too_many_requests)
            name.contains("FirebaseNetworkException") || message.contains("network", ignoreCase = true) ->
                UiText.StringResource(R.string.error_auth_network_error)
            name.contains("GetCredentialCancellationException") || message.contains("canceled", ignoreCase = true) ->
                UiText.StringResource(R.string.error_auth_cancelled)
            else -> UiText.StringResource(R.string.error_auth_generic)
        }
    }
}
