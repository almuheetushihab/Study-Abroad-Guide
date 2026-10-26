package com.shihab.studyabroad.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.shihab.studyabroad.data.local.AppDatabase
import com.shihab.studyabroad.domain.model.Track
import com.shihab.studyabroad.domain.model.UserProfile
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "study_abroad_preferences")

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appDatabase: AppDatabase,
) : UserPreferencesRepository {

    private val json = Json { ignoreUnknownKeys = true }

    private val onboardingDoneKey = booleanPreferencesKey("onboarding_done")
    private val signedInKey = booleanPreferencesKey("signed_in")
    private val selectedTrackKey = stringPreferencesKey("selected_track")
    private val userProfileJsonKey = stringPreferencesKey("user_profile_json")

    private val deadlineRemindersKey = booleanPreferencesKey("deadline_reminders_enabled")
    private val newScholarshipsKey = booleanPreferencesKey("new_scholarships_enabled")
    private val appNewsKey = booleanPreferencesKey("app_news_enabled")

    override val isOnboardingDone: Flow<Boolean> = context.preferencesDataStore.data.map { preferences ->
        preferences[onboardingDoneKey] ?: false
    }

    override val isSignedIn: Flow<Boolean> = context.preferencesDataStore.data.map { preferences ->
        preferences[signedInKey] ?: false
    }

    override val selectedTrack: Flow<Track?> = context.preferencesDataStore.data.map { preferences ->
        preferences[selectedTrackKey]?.let {
            try {
                Track.valueOf(it)
            } catch (_: Exception) {
                null
            }
        }
    }

    override val userProfile: Flow<UserProfile?> = context.preferencesDataStore.data.map { preferences ->
        preferences[userProfileJsonKey]?.let { jsonString ->
            try {
                json.decodeFromString<UserProfile>(jsonString)
            } catch (_: Exception) {
                null
            }
        }
    }

    override val deadlineRemindersEnabled: Flow<Boolean> = context.preferencesDataStore.data.map { preferences ->
        preferences[deadlineRemindersKey] ?: true
    }

    override val newScholarshipsEnabled: Flow<Boolean> = context.preferencesDataStore.data.map { preferences ->
        preferences[newScholarshipsKey] ?: true
    }

    override val appNewsEnabled: Flow<Boolean> = context.preferencesDataStore.data.map { preferences ->
        preferences[appNewsKey] ?: true
    }

    override suspend fun setOnboardingDone(done: Boolean) {
        context.preferencesDataStore.edit { preferences ->
            preferences[onboardingDoneKey] = done
        }
    }

    override suspend fun setSignedIn(signedIn: Boolean) {
        context.preferencesDataStore.edit { preferences ->
            preferences[signedInKey] = signedIn
        }
    }

    override suspend fun setSelectedTrack(track: Track) {
        context.preferencesDataStore.edit { preferences ->
            preferences[selectedTrackKey] = track.name
        }
    }

    override suspend fun saveUserProfile(profile: UserProfile) {
        val jsonString = json.encodeToString(profile)
        context.preferencesDataStore.edit { preferences ->
            preferences[userProfileJsonKey] = jsonString
            preferences[selectedTrackKey] = profile.targetTrack.name
        }
    }

    override suspend fun setDeadlineRemindersEnabled(enabled: Boolean) {
        context.preferencesDataStore.edit { preferences ->
            preferences[deadlineRemindersKey] = enabled
        }
    }

    override suspend fun setNewScholarshipsEnabled(enabled: Boolean) {
        context.preferencesDataStore.edit { preferences ->
            preferences[newScholarshipsKey] = enabled
        }
    }

    override suspend fun setAppNewsEnabled(enabled: Boolean) {
        context.preferencesDataStore.edit { preferences ->
            preferences[appNewsKey] = enabled
        }
    }

    override suspend fun clearSession() {
        context.preferencesDataStore.edit { preferences ->
            preferences.remove(signedInKey)
            preferences.remove(selectedTrackKey)
            preferences.remove(userProfileJsonKey)
            preferences.remove(deadlineRemindersKey)
            preferences.remove(newScholarshipsKey)
            preferences.remove(appNewsKey)
        }
    }

    override suspend fun clearAllData() {
        try {
            appDatabase.clearAllTables()
        } catch (_: Exception) {
            // Best effort database clear
        }
        context.preferencesDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
