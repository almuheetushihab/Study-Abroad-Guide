package com.shihab.studyabroad.data.repository

import app.cash.turbine.test
import com.shihab.studyabroad.data.local.dao.SavedItemDao
import com.shihab.studyabroad.data.local.entity.SavedItemEntity
import com.shihab.studyabroad.domain.model.SavedType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SavedRepositoryTest {

    private val fixedClock = object : Clock {
        override fun now(): Instant = Instant.fromEpochMilliseconds(1000L)
    }

    private lateinit var dao: FakeSavedItemDao
    private lateinit var repository: SavedRepositoryImpl

    @Before
    fun setUp() {
        dao = FakeSavedItemDao()
        repository = SavedRepositoryImpl(dao, fixedClock)
    }

    @Test
    fun `saving and unsaving item updates observeSavedItems and isSaved flow`() = runTest {
        repository.observeSavedItems().test {
            assertEquals(emptyList<Any>(), awaitItem())

            repository.saveItem(SavedType.COUNTRY, "de")

            val savedList = awaitItem()
            assertEquals(1, savedList.size)
            assertEquals(SavedType.COUNTRY, savedList.first().type)
            assertEquals("de", savedList.first().itemId)

            repository.unsaveItem(SavedType.COUNTRY, "de")

            val emptyListAfterUnsave = awaitItem()
            assertTrue(emptyListAfterUnsave.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleSaved toggles item save state`() = runTest {
        repository.isSaved(SavedType.SCHOLARSHIP, "daad-epos").test {
            assertFalse(awaitItem())

            repository.toggleSaved(SavedType.SCHOLARSHIP, "daad-epos")
            assertTrue(awaitItem())

            repository.toggleSaved(SavedType.SCHOLARSHIP, "daad-epos")
            assertFalse(awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    private class FakeSavedItemDao : SavedItemDao {
        private val items = MutableStateFlow<List<SavedItemEntity>>(emptyList())

        override fun observeAllSavedItems(): Flow<List<SavedItemEntity>> = items.asStateFlow()

        override fun observeIsSaved(type: String, itemId: String): Flow<Boolean> = items.map { list ->
            list.any { it.type == type && it.itemId == itemId }
        }

        override suspend fun isSaved(type: String, itemId: String): Boolean = items.value.any { it.type == type && it.itemId == itemId }

        override suspend fun insertSavedItem(item: SavedItemEntity) {
            items.value = items.value.filterNot { it.type == item.type && it.itemId == item.itemId } + item
        }

        override suspend fun deleteSavedItem(type: String, itemId: String) {
            items.value = items.value.filterNot { it.type == type && it.itemId == itemId }
        }
    }
}
