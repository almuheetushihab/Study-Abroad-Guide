package com.shihab.studyabroad.core.common

import com.google.firebase.auth.FirebaseAuthException
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.NoGoogleAccountException

object AuthErrorMapper {
    fun mapExceptionToUiText(throwable: Throwable?): UiText {
        if (throwable == null) return UiText.StringResource(R.string.error_auth_generic)

        if (throwable is NoGoogleAccountException) {
            return UiText.StringResource(R.string.error_auth_no_google_account)
        }

        val name = throwable.javaClass.simpleName
        val message = throwable.message ?: ""
        val errorCode = (throwable as? FirebaseAuthException)?.errorCode ?: ""
        val causeName = throwable.cause?.javaClass?.simpleName ?: ""
        val causeMessage = throwable.cause?.message ?: ""

        val combined = "$name $message $errorCode $causeName $causeMessage"

        return when {
            combined.contains("NoGoogleAccountException") ||
                combined.contains("NoCredentialException") ||
                combined.contains("No credentials available", ignoreCase = true) ||
                combined.contains("no google account", ignoreCase = true) ->
                UiText.StringResource(R.string.error_auth_no_google_account)

            combined.contains("FirebaseAuthRecentLoginRequiredException") ||
                combined.contains("ERROR_REQUIRES_RECENT_LOGIN") ||
                combined.contains("recent", ignoreCase = true) ||
                combined.contains("sensitive", ignoreCase = true) ||
                combined.contains("log in again", ignoreCase = true) ->
                UiText.StringResource(R.string.error_auth_requires_recent_login)

            combined.contains("phone", ignoreCase = true) && combined.contains("invalid", ignoreCase = true) ->
                UiText.StringResource(R.string.error_invalid_phone)

            combined.contains("FirebaseAuthInvalidCredentialsException") ||
                combined.contains("ERROR_INVALID_CREDENTIAL") ||
                combined.contains("invalid code", ignoreCase = true) ||
                combined.contains("wrong code", ignoreCase = true) ->
                UiText.StringResource(R.string.error_auth_invalid_code)

            combined.contains("FirebaseTooManyRequestsException") ||
                combined.contains("ERROR_TOO_MANY_REQUESTS") ||
                combined.contains("too many", ignoreCase = true) ->
                UiText.StringResource(R.string.error_auth_too_many_requests)

            combined.contains("FirebaseNetworkException") ||
                combined.contains("ERROR_NETWORK_REQUEST_FAILED") ||
                combined.contains("network", ignoreCase = true) ->
                UiText.StringResource(R.string.error_auth_network_error)

            combined.contains("GetCredentialCancellationException") ||
                combined.contains("canceled", ignoreCase = true) ||
                combined.contains("cancelled", ignoreCase = true) ->
                UiText.StringResource(R.string.error_auth_cancelled)

            else -> UiText.StringResource(R.string.error_auth_generic)
        }
    }
}
