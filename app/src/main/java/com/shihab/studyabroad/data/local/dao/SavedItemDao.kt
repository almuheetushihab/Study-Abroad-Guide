package com.shihab.studyabroad.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shihab.studyabroad.data.local.entity.SavedItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedItemDao {
    @Query("SELECT * FROM saved_items ORDER BY savedAt DESC")
    fun observeAllSavedItems(): Flow<List<SavedItemEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_items WHERE type = :type AND itemId = :itemId)")
    fun observeIsSaved(type: String, itemId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_items WHERE type = :type AND itemId = :itemId)")
    suspend fun isSaved(type: String, itemId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedItem(item: SavedItemEntity)

    @Query("DELETE FROM saved_items WHERE type = :type AND itemId = :itemId")
    suspend fun deleteSavedItem(type: String, itemId: String)
}
