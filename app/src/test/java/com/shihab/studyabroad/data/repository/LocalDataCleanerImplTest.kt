package com.shihab.studyabroad.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.shihab.studyabroad.core.common.AppResult
import com.shihab.studyabroad.core.common.DispatcherProvider
import com.shihab.studyabroad.data.local.AppDatabase
import com.shihab.studyabroad.data.local.datastore.UserPreferencesRepositoryImpl
import com.shihab.studyabroad.domain.model.Track
import com.shihab.studyabroad.domain.model.UserProfile
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
class LocalDataCleanerImplTest {

    @get:Rule
    val tmpFolder = TemporaryFolder()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val testDispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher get() = testDispatcher
        override val io: CoroutineDispatcher get() = testDispatcher
        override val default: CoroutineDispatcher get() = testDispatcher
        override val unconfined: CoroutineDispatcher get() = testDispatcher
    }

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var appDatabase: AppDatabase
    private lateinit var userPreferencesRepository: UserPreferencesRepositoryImpl
    private lateinit var localDataCleaner: LocalDataCleanerImpl

    @Before
    fun setUp() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { tmpFolder.newFile("test_study_abroad_preferences.preferences_pb") },
        )
        appDatabase = mockk(relaxed = true)

        userPreferencesRepository = UserPreferencesRepositoryImpl(dataStore)
        localDataCleaner = LocalDataCleanerImpl(
            dataStore = dataStore,
            appDatabase = appDatabase,
            dispatcherProvider = testDispatcherProvider,
        )
    }

    @Test
    fun `clearAllLocalData clears Room tables and preferences in shared DataStore`() = runTest(testDispatcher) {
        val testProfile = UserProfile(level = "Master", targetTrack = Track.GRADUATE)
        userPreferencesRepository.setOnboardingDone(true)
        userPreferencesRepository.setSignedIn(true)
        userPreferencesRepository.saveUserProfile(testProfile)

        assertEquals(true, userPreferencesRepository.isOnboardingDone.first())
        assertEquals(true, userPreferencesRepository.isSignedIn.first())

        val result = localDataCleaner.clearAllLocalData()

        assertTrue(result is AppResult.Success)
        verify(exactly = 1) { appDatabase.clearAllTables() }

        assertEquals(false, userPreferencesRepository.isOnboardingDone.first())
        assertEquals(false, userPreferencesRepository.isSignedIn.first())
        assertNull(userPreferencesRepository.userProfile.first())
    }

    @Test
    fun `clearAllLocalData returns AppResult Error when database clear fails and does not throw exception`() = runTest(testDispatcher) {
        val dbError = RuntimeException("Room database wipe failure")
        every { appDatabase.clearAllTables() } throws dbError

        val result = localDataCleaner.clearAllLocalData()

        assertTrue(result is AppResult.Error)
        val error = result as AppResult.Error
        assertEquals(dbError, error.throwable)
    }
}
