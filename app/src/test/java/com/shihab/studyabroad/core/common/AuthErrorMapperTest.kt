package com.shihab.studyabroad.core.common

import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.ui.UiText
import com.shihab.studyabroad.domain.model.NoGoogleAccountException
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthErrorMapperTest {

    @Test
    fun `maps null throwable to generic error`() {
        val result = AuthErrorMapper.mapExceptionToUiText(null)
        assertEquals(UiText.StringResource(R.string.error_auth_generic), result)
    }

    @Test
    fun `maps invalid code exception to invalid code string resource`() {
        val exception = Exception("FirebaseAuthInvalidCredentialsException: Invalid verification code")
        val result = AuthErrorMapper.mapExceptionToUiText(exception)
        assertEquals(UiText.StringResource(R.string.error_auth_invalid_code), result)
    }

    @Test
    fun `maps requires recent login exception to recent login string resource`() {
        val exception =
            Exception("This operation is sensitive and requires recent authentication. Log in again before retrying this request.")
        val result = AuthErrorMapper.mapExceptionToUiText(exception)
        assertEquals(UiText.StringResource(R.string.error_auth_requires_recent_login), result)
    }

    @Test
    fun `maps ERROR_REQUIRES_RECENT_LOGIN exception code to recent login string resource`() {
        val exception = Exception("An internal error has occurred. [ ERROR_REQUIRES_RECENT_LOGIN ]")
        val result = AuthErrorMapper.mapExceptionToUiText(exception)
        assertEquals(UiText.StringResource(R.string.error_auth_requires_recent_login), result)
    }

    @Test
    fun `maps cancellation exception to cancelled string resource`() {
        val exception = Exception("GetCredentialCancellationException: User canceled sign-in")
        val result = AuthErrorMapper.mapExceptionToUiText(exception)
        assertEquals(UiText.StringResource(R.string.error_auth_cancelled), result)
    }

    @Test
    fun `maps network exception to network error string resource`() {
        val exception = Exception("FirebaseNetworkException: A network error occurred")
        val result = AuthErrorMapper.mapExceptionToUiText(exception)
        assertEquals(UiText.StringResource(R.string.error_auth_network_error), result)
    }

    @Test
    fun `maps NoGoogleAccountException to no google account string resource`() {
        val exception = NoGoogleAccountException()
        val result = AuthErrorMapper.mapExceptionToUiText(exception)
        assertEquals(UiText.StringResource(R.string.error_auth_no_google_account), result)
    }

    @Test
    fun `maps NoCredentialException message to no google account string resource`() {
        val exception = Exception("androidx.credentials.exceptions.NoCredentialException: No credentials available")
        val result = AuthErrorMapper.mapExceptionToUiText(exception)
        assertEquals(UiText.StringResource(R.string.error_auth_no_google_account), result)
    }
}
