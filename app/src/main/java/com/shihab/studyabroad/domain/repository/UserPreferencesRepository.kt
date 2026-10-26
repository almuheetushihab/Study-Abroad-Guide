package com.shihab.studyabroad.domain.repository

import com.shihab.studyabroad.domain.model.Track
import com.shihab.studyabroad.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isOnboardingDone: Flow<Boolean>
    val isSignedIn: Flow<Boolean>
    val selectedTrack: Flow<Track?>
    val userProfile: Flow<UserProfile?>

    val deadlineRemindersEnabled: Flow<Boolean>
    val newScholarshipsEnabled: Flow<Boolean>
    val appNewsEnabled: Flow<Boolean>

    suspend fun setOnboardingDone(done: Boolean)
    suspend fun setSignedIn(signedIn: Boolean)
    suspend fun setSelectedTrack(track: Track)
    suspend fun saveUserProfile(profile: UserProfile)
    suspend fun setDeadlineRemindersEnabled(enabled: Boolean)
    suspend fun setNewScholarshipsEnabled(enabled: Boolean)
    suspend fun setAppNewsEnabled(enabled: Boolean)
    suspend fun clearSession()
    suspend fun clearAllData()
}
