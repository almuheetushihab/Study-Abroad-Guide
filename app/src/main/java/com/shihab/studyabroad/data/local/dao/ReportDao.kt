package com.shihab.studyabroad.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shihab.studyabroad.data.local.entity.ReportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity)

    @Query("SELECT * FROM reports ORDER BY createdAt DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Query("DELETE FROM reports")
    suspend fun deleteAllReports()
}
