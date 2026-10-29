package com.shihab.studyabroad.data.repository

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.shihab.studyabroad.BuildConfig
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.core.common.CrashReporter
import com.shihab.studyabroad.core.common.PhoneAuthLauncher
import com.shihab.studyabroad.core.common.PhoneNumberNormalizer
import com.shihab.studyabroad.domain.model.AuthProvider
import com.shihab.studyabroad.domain.model.AuthUser
import com.shihab.studyabroad.domain.model.DeleteAccountResult
import com.shihab.studyabroad.domain.repository.AuthRepository
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class FirebaseAuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val phoneAuthLauncher: PhoneAuthLauncher,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val crashReporter: CrashReporter,
) : AuthRepository {

    private val firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    private var currentVerificationId: String? = null
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null

    override val currentUser: Flow<AuthUser?> = callbackFlow {
        val auth = firebaseAuth
        val listener = FirebaseAuth.AuthStateListener { fa ->
            val user = fa.currentUser
            val authUser = user?.let {
                val provider = determineProvider(it)
                AuthUser(
                    id = it.uid,
                    displayName = it.displayName,
                    phone = it.phoneNumber,
                    email = it.email,
                    provider = provider,
                )
            }
            trySend(authUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override val isSignedIn: Flow<Boolean> = currentUser.map { it != null }

    override suspend fun sendOtp(phoneNumber: String): AppResult<Unit> = sendOtpInternal(phoneNumber, isResend = false)

    override suspend fun resendOtp(phoneNumber: String): AppResult<Unit> = sendOtpInternal(phoneNumber, isResend = true)

    private suspend fun sendOtpInternal(phoneNumber: String, isResend: Boolean): AppResult<Unit> {
        val activity = phoneAuthLauncher.getActivity()
            ?: return AppResult.Error(IllegalStateException("Activity unavailable for phone verification"))

        val normalizedPhone = PhoneNumberNormalizer.normalize(phoneNumber)

        return suspendCancellableCoroutine { continuation ->
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                if (continuation.isActive) {
                                    continuation.resume(AppResult.Success(Unit))
                                }
                            }
                        }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    logAndReport(e)
                    if (continuation.isActive) {
                        continuation.resume(AppResult.Error(e))
                    }
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    currentVerificationId = verificationId
                    forceResendingToken = token
                    if (continuation.isActive) {
                        continuation.resume(AppResult.Success(Unit))
                    }
                }
            }

            val builder = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(normalizedPhone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)

            if (isResend && forceResendingToken != null) {
                builder.setForceResendingToken(forceResendingToken!!)
            }

            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        }
    }

    override suspend fun verifyOtp(otp: String): AppResult<Unit> {
        val verificationId = currentVerificationId
            ?: return AppResult.Error(IllegalStateException("Verification ID missing. Send OTP first."))

        val credential = PhoneAuthProvider.getCredential(verificationId, otp)

        return suspendCancellableCoroutine { continuation ->
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        userPreferencesRepository
                        continuation.resume(AppResult.Success(Unit))
                    } else {
                        val exception = task.exception ?: Exception("Verification failed")
                        logAndReport(exception)
                        continuation.resume(AppResult.Error(exception))
                    }
                }
        }
    }

    override suspend fun signInWithGoogle(): AppResult<Unit> {
        val activity = phoneAuthLauncher.getActivity()
            ?: return AppResult.Error(IllegalStateException("Activity unavailable for Google sign-in"))

        val webClientId = context.getString(R.string.default_web_client_id)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(context = activity, request = request)

            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
            val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

            suspendCancellableCoroutine { continuation ->
                firebaseAuth.signInWithCredential(authCredential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(AppResult.Success(Unit))
                        } else {
                            val exception = task.exception ?: Exception("Google sign in failed")
                            logAndReport(exception)
                            continuation.resume(AppResult.Error(exception))
                        }
                    }
            }
        } catch (e: GetCredentialCancellationException) {
            logAndReport(e)
            AppResult.Error(e)
        } catch (e: NoCredentialException) {
            logAndReport(e)
            AppResult.Error(e)
        } catch (e: Exception) {
            logAndReport(e)
            AppResult.Error(e)
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
        try {
            val credentialManager = CredentialManager.create(context)
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (_: Exception) {
            // Ignore clear credential state failures
        }
    }

    override suspend fun deleteAccount(): DeleteAccountResult {
        val user = firebaseAuth.currentUser ?: return DeleteAccountResult.Success

        return suspendCancellableCoroutine { continuation ->
            user.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(DeleteAccountResult.Success)
                } else {
                    val exception = task.exception ?: Exception("Failed to delete user")
                    logAndReport(exception)

                    val errorCode = (exception as? FirebaseAuthException)?.errorCode ?: ""
                    val name = exception.javaClass.simpleName
                    val message = exception.message ?: ""
                    val causeMessage = exception.cause?.message ?: ""

                    val isRecentLoginRequired = exception is FirebaseAuthRecentLoginRequiredException ||
                        errorCode == "ERROR_REQUIRES_RECENT_LOGIN" ||
                        name.contains("FirebaseAuthRecentLoginRequiredException") ||
                        message.contains("ERROR_REQUIRES_RECENT_LOGIN") ||
                        message.contains("recent", ignoreCase = true) ||
                        causeMessage.contains("ERROR_REQUIRES_RECENT_LOGIN")

                    if (isRecentLoginRequired) {
                        val provider = determineProvider(user)
                        continuation.resume(DeleteAccountResult.ReauthenticationRequired(provider))
                    } else {
                        continuation.resume(DeleteAccountResult.Error(exception))
                    }
                }
            }
        }
    }

    override suspend fun reauthenticateAndDeleteWithGoogle(): AppResult<Unit> {
        val user = firebaseAuth.currentUser
            ?: return AppResult.Error(IllegalStateException("No user logged in"))

        val activity = phoneAuthLauncher.getActivity()
            ?: return AppResult.Error(IllegalStateException("Activity unavailable for Google re-authentication"))

        val webClientId = context.getString(R.string.default_web_client_id)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(context = activity, request = request)

            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
            val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

            suspendCancellableCoroutine { continuation ->
                user.reauthenticate(authCredential)
                    .addOnCompleteListener { reauthTask ->
                        if (reauthTask.isSuccessful) {
                            user.delete().addOnCompleteListener { deleteTask ->
                                if (deleteTask.isSuccessful) {
                                    continuation.resume(AppResult.Success(Unit))
                                } else {
                                    val ex = deleteTask.exception ?: Exception("Delete failed after re-auth")
                                    logAndReport(ex)
                                    continuation.resume(AppResult.Error(ex))
                                }
                            }
                        } else {
                            val ex = reauthTask.exception ?: Exception("Google re-authentication failed")
                            logAndReport(ex)
                            continuation.resume(AppResult.Error(ex))
                        }
                    }
            }
        } catch (e: GetCredentialCancellationException) {
            logAndReport(e)
            AppResult.Error(e)
        } catch (e: NoCredentialException) {
            logAndReport(e)
            AppResult.Error(e)
        } catch (e: Exception) {
            logAndReport(e)
            AppResult.Error(e)
        }
    }

    override suspend fun reauthenticateAndDeleteWithPhone(otp: String): AppResult<Unit> {
        val user = firebaseAuth.currentUser
            ?: return AppResult.Error(IllegalStateException("No user logged in"))

        val verificationId = currentVerificationId
            ?: return AppResult.Error(IllegalStateException("Verification ID missing. Send OTP first."))

        val credential = PhoneAuthProvider.getCredential(verificationId, otp)

        return suspendCancellableCoroutine { continuation ->
            user.reauthenticate(credential)
                .addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        user.delete().addOnCompleteListener { deleteTask ->
                            if (deleteTask.isSuccessful) {
                                continuation.resume(AppResult.Success(Unit))
                            } else {
                                val ex = deleteTask.exception ?: Exception("Delete failed after re-auth")
                                logAndReport(ex)
                                continuation.resume(AppResult.Error(ex))
                            }
                        }
                    } else {
                        val ex = reauthTask.exception ?: Exception("Phone re-authentication failed")
                        logAndReport(ex)
                        continuation.resume(AppResult.Error(ex))
                    }
                }
        }
    }

    private fun determineProvider(user: FirebaseUser): AuthProvider {
        val providerId = user.providerData.firstOrNull { it.providerId != "firebase" }?.providerId
        return when (providerId) {
            GoogleAuthProvider.PROVIDER_ID -> AuthProvider.GOOGLE

            PhoneAuthProvider.PROVIDER_ID -> AuthProvider.PHONE

            else -> when {
                !user.phoneNumber.isNullOrBlank() -> AuthProvider.PHONE
                !user.email.isNullOrBlank() -> AuthProvider.GOOGLE
                else -> AuthProvider.UNKNOWN
            }
        }
    }

    private fun logAndReport(e: Throwable) {
        if (BuildConfig.DEBUG) {
            val name = e.javaClass.simpleName
            val errorCode = (e as? FirebaseAuthException)?.errorCode ?: "NONE"
            android.util.Log.w("FirebaseAuthRepo", "Auth operation failed. Exception: $name, ErrorCode: $errorCode", e)
        }
        crashReporter.recordNonFatal(e)
    }
}
