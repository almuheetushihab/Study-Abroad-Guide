package com.shihab.studyabroad.domain.repository

import com.shihab.studyabroad.domain.model.SavedItem
import com.shihab.studyabroad.domain.model.SavedType
import kotlinx.coroutines.flow.Flow

interface SavedRepository {
    fun observeSavedItems(): Flow<List<SavedItem>>
    fun isSaved(type: SavedType, itemId: String): Flow<Boolean>
    suspend fun toggleSaved(type: SavedType, itemId: String)
    suspend fun saveItem(type: SavedType, itemId: String)
    suspend fun unsaveItem(type: SavedType, itemId: String)
}
