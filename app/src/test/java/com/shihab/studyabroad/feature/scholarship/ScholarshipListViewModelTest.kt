package com.shihab.studyabroad.feature.scholarship

import app.cash.turbine.test
import com.shihab.studyabroad.data.repository.FakeAnalyticsTracker
import com.shihab.studyabroad.data.repository.FakeScholarshipRepository
import com.shihab.studyabroad.domain.usecase.GetScholarshipStatusUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ScholarshipListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = FakeScholarshipRepository()
    private val analyticsTracker = FakeAnalyticsTracker()

    private val fixedClock = object : Clock {
        override fun now(): Instant = Instant.parse("2026-10-23T00:00:00Z")
    }
    private val getScholarshipStatusUseCase = GetScholarshipStatusUseCase(
        clock = fixedClock,
        timeZone = TimeZone.UTC,
    )

    private lateinit var viewModel: ScholarshipListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ScholarshipListViewModel(
            scholarshipRepository = repository,
            getScholarshipStatusUseCase = getScholarshipStatusUseCase,
            analyticsTracker = analyticsTracker,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState emits Content with full sample list initially`() = runTest {
        viewModel.uiState.test {
            // First item should be Loading
            assertEquals(ScholarshipListUiState.Loading, awaitItem())

            // Advance time for StateFlow combine
            testDispatcher.scheduler.advanceUntilIdle()

            val content = awaitItem() as ScholarshipListUiState.Content
            assertTrue(content.data.items.isNotEmpty())
            assertEquals("All", content.data.selectedCountryFilter)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `filtering by country updates content state with filtered items`() = runTest {
        viewModel.uiState.test {
            assertEquals(ScholarshipListUiState.Loading, awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()

            val initialContent = awaitItem() as ScholarshipListUiState.Content
            assertTrue(initialContent.data.items.size >= 5)

            // Select Germany
            viewModel.onSelectCountryFilter("Germany")
            testDispatcher.scheduler.advanceUntilIdle()

            val filteredContent = awaitItem() as ScholarshipListUiState.Content
            assertEquals("Germany", filteredContent.data.selectedCountryFilter)
            assertTrue(filteredContent.data.items.all { it.scholarship.countryId == "de" })

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `filtering by level updates content state`() = runTest {
        viewModel.uiState.test {
            assertEquals(ScholarshipListUiState.Loading, awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            awaitItem()

            viewModel.onSelectLevelFilter("PhD")
            testDispatcher.scheduler.advanceUntilIdle()

            val content = awaitItem() as ScholarshipListUiState.Content
            assertEquals("PhD", content.data.selectedLevelFilter)
            assertTrue(content.data.items.all { it.scholarship.level.contains("PhD") })

            cancelAndIgnoreRemainingEvents()
        }
    }
}
