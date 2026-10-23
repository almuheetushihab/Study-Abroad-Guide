package com.shihab.studyabroad.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shihab.studyabroad.data.local.entity.ChecklistProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistProgressDao {
    @Query("SELECT * FROM checklist_progress")
    fun observeAllProgress(): Flow<List<ChecklistProgressEntity>>

    @Query("SELECT * FROM checklist_progress WHERE itemId = :itemId")
    suspend fun getProgress(itemId: String): ChecklistProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: ChecklistProgressEntity)

    @Query("DELETE FROM checklist_progress WHERE itemId = :itemId")
    suspend fun deleteProgress(itemId: String)
}
