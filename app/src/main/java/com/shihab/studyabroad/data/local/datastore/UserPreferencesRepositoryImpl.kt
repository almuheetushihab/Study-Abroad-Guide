package com.shihab.studyabroad.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.shihab.studyabroad.domain.model.Track
import com.shihab.studyabroad.domain.model.UserProfile
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) : UserPreferencesRepository {

    private val json = Json { ignoreUnknownKeys = true }

    private val onboardingDoneKey = booleanPreferencesKey("onboarding_done")
    private val signedInKey = booleanPreferencesKey("signed_in")
    private val selectedTrackKey = stringPreferencesKey("selected_track")
    private val userProfileJsonKey = stringPreferencesKey("user_profile_json")

    private val deadlineRemindersKey = booleanPreferencesKey("deadline_reminders_enabled")
    private val newScholarshipsKey = booleanPreferencesKey("new_scholarships_enabled")
    private val appNewsKey = booleanPreferencesKey("app_news_enabled")

    override val isOnboardingDone: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[onboardingDoneKey] ?: false
    }

    override val isSignedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[signedInKey] ?: false
    }

    override val selectedTrack: Flow<Track?> = dataStore.data.map { preferences ->
        preferences[selectedTrackKey]?.let {
            try {
                Track.valueOf(it)
            } catch (_: Exception) {
                null
            }
        }
    }

    override val userProfile: Flow<UserProfile?> = dataStore.data.map { preferences ->
        preferences[userProfileJsonKey]?.let { jsonString ->
            try {
                json.decodeFromString<UserProfile>(jsonString)
            } catch (_: Exception) {
                null
            }
        }
    }

    override val deadlineRemindersEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[deadlineRemindersKey] ?: true
    }

    override val newScholarshipsEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[newScholarshipsKey] ?: true
    }

    override val appNewsEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[appNewsKey] ?: true
    }

    override suspend fun setOnboardingDone(done: Boolean) {
        dataStore.edit { preferences ->
            preferences[onboardingDoneKey] = done
        }
    }

    override suspend fun setSignedIn(signedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[signedInKey] = signedIn
        }
    }

    override suspend fun setSelectedTrack(track: Track) {
        dataStore.edit { preferences ->
            preferences[selectedTrackKey] = track.name
        }
    }

    override suspend fun saveUserProfile(profile: UserProfile) {
        val jsonString = json.encodeToString(profile)
        dataStore.edit { preferences ->
            preferences[userProfileJsonKey] = jsonString
            preferences[selectedTrackKey] = profile.targetTrack.name
        }
    }

    override suspend fun setDeadlineRemindersEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[deadlineRemindersKey] = enabled
        }
    }

    override suspend fun setNewScholarshipsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[newScholarshipsKey] = enabled
        }
    }

    override suspend fun setAppNewsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[appNewsKey] = enabled
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(signedInKey)
            preferences.remove(selectedTrackKey)
            preferences.remove(userProfileJsonKey)
            preferences.remove(deadlineRemindersKey)
            preferences.remove(newScholarshipsKey)
            preferences.remove(appNewsKey)
        }
    }
}
