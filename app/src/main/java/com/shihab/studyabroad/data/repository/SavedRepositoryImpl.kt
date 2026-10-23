package com.shihab.studyabroad.data.repository

import com.shihab.studyabroad.data.local.dao.SavedItemDao
import com.shihab.studyabroad.data.mapper.toDomain
import com.shihab.studyabroad.data.mapper.toEntity
import com.shihab.studyabroad.domain.model.SavedItem
import com.shihab.studyabroad.domain.model.SavedType
import com.shihab.studyabroad.domain.repository.SavedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedRepositoryImpl @Inject constructor(private val savedItemDao: SavedItemDao) : SavedRepository {

    override fun observeSavedItems(): Flow<List<SavedItem>> = savedItemDao.observeAllSavedItems().map { entities ->
        entities.map { it.toDomain() }
    }

    override fun isSaved(type: SavedType, itemId: String): Flow<Boolean> = savedItemDao.observeIsSaved(type.name, itemId)

    override suspend fun toggleSaved(type: SavedType, itemId: String) {
        if (savedItemDao.isSaved(type.name, itemId)) {
            savedItemDao.deleteSavedItem(type.name, itemId)
        } else {
            savedItemDao.insertSavedItem(
                SavedItem(
                    type = type,
                    itemId = itemId,
                    savedAt = System.currentTimeMillis(),
                ).toEntity(),
            )
        }
    }

    override suspend fun saveItem(type: SavedType, itemId: String) {
        savedItemDao.insertSavedItem(
            SavedItem(
                type = type,
                itemId = itemId,
                savedAt = System.currentTimeMillis(),
            ).toEntity(),
        )
    }

    override suspend fun unsaveItem(type: SavedType, itemId: String) {
        savedItemDao.deleteSavedItem(type.name, itemId)
    }
}
