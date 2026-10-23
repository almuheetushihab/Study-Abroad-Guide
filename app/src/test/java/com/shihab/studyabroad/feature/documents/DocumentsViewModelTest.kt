package com.shihab.studyabroad.feature.documents

import app.cash.turbine.test
import com.shihab.studyabroad.core.common.UrlOpener
import com.shihab.studyabroad.domain.model.ChecklistItem
import com.shihab.studyabroad.domain.model.FundingGoal
import com.shihab.studyabroad.domain.model.TestType
import com.shihab.studyabroad.domain.model.Track
import com.shihab.studyabroad.domain.model.UserProfile
import com.shihab.studyabroad.domain.repository.ChecklistRepository
import com.shihab.studyabroad.domain.repository.UserProfileRepository
import com.shihab.studyabroad.domain.usecase.ObserveChecklistProgressUseCase
import com.shihab.studyabroad.domain.usecase.ToggleChecklistItemUseCase
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DocumentsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val urlOpener = mockk<UrlOpener>(relaxed = true)

    private val fakeUserProfileRepository = object : UserProfileRepository {
        private val profileFlow = MutableStateFlow<UserProfile?>(
            UserProfile(
                id = "u1",
                name = "Test",
                targetTrack = Track.GRADUATE,
                level = "MSc",
                subject = "CSE",
                cgpa = 3.5,
                cgpaScale = 4.0,
                testType = TestType.NONE,
                testScore = null,
                experienceYears = 0,
                age = 24,
                budgetRange = "10 Lakh",
                goal = FundingGoal.SCHOLARSHIP,
                targetCountries = listOf("de"),
            ),
        )
        override fun getUserProfile(): Flow<UserProfile?> = profileFlow.asStateFlow()
        override suspend fun updateUserProfile(profile: UserProfile) {}
    }

    private val fakeChecklistRepository = object : ChecklistRepository {
        private val itemsFlow = MutableStateFlow(
            listOf(
                ChecklistItem("1", "Passport", "Desc", true, "Category"),
                ChecklistItem("2", "Degree", "Desc", false, "Category"),
                ChecklistItem("3", "SOP", "Desc", false, "Category"),
                ChecklistItem("4", "IELTS", "Desc", true, "Category"),
            ),
        )

        override fun getChecklistItems(track: Track?): Flow<List<ChecklistItem>> = itemsFlow.asStateFlow()

        override suspend fun toggleItemCompleted(id: String) {
            itemsFlow.value = itemsFlow.value.map {
                if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
            }
        }
    }

    private val observeChecklistProgressUseCase = ObserveChecklistProgressUseCase(
        checklistRepository = fakeChecklistRepository,
        userProfileRepository = fakeUserProfileRepository,
    )
    private val toggleChecklistItemUseCase = ToggleChecklistItemUseCase(fakeChecklistRepository)

    private lateinit var viewModel: DocumentsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DocumentsViewModel(
            observeChecklistProgressUseCase = observeChecklistProgressUseCase,
            toggleChecklistItemUseCase = toggleChecklistItemUseCase,
            urlOpener = urlOpener,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState emits correct progress percentage and updates when item is toggled`() = runTest {
        viewModel.uiState.test {
            assertEquals(DocumentsUiState.Loading, awaitItem())

            testDispatcher.scheduler.advanceUntilIdle()

            val content = awaitItem() as DocumentsUiState.Content
            assertEquals(4, content.data.totalCount)
            assertEquals(2, content.data.completedCount)
            assertEquals(50, content.data.progressPercentage)

            // Toggle item "2" (Degree) to completed
            viewModel.onToggleItem("2")
            testDispatcher.scheduler.advanceUntilIdle()

            val updatedContent = awaitItem() as DocumentsUiState.Content
            assertEquals(3, updatedContent.data.completedCount)
            assertEquals(75, updatedContent.data.progressPercentage)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
