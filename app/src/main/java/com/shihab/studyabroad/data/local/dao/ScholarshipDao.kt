package com.shihab.studyabroad.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shihab.studyabroad.data.local.entity.ScholarshipEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScholarshipDao {
    @Query("SELECT * FROM scholarships")
    fun getAllScholarships(): Flow<List<ScholarshipEntity>>

    @Query("SELECT * FROM scholarships WHERE id = :id")
    fun getScholarshipById(id: String): Flow<ScholarshipEntity?>

    @Query("SELECT * FROM scholarships WHERE countryId = :countryId")
    fun getScholarshipsByCountry(countryId: String): Flow<List<ScholarshipEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScholarships(scholarships: List<ScholarshipEntity>)
}
