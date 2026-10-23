package com.shihab.studyabroad.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shihab.studyabroad.data.local.dao.ChecklistProgressDao
import com.shihab.studyabroad.data.local.dao.CountryDao
import com.shihab.studyabroad.data.local.dao.SavedItemDao
import com.shihab.studyabroad.data.local.dao.ScholarshipDao
import com.shihab.studyabroad.data.local.entity.ChecklistProgressEntity
import com.shihab.studyabroad.data.local.entity.CountryEntity
import com.shihab.studyabroad.data.local.entity.SavedItemEntity
import com.shihab.studyabroad.data.local.entity.ScholarshipEntity

@Database(
    entities = [
        ScholarshipEntity::class,
        CountryEntity::class,
        SavedItemEntity::class,
        ChecklistProgressEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scholarshipDao(): ScholarshipDao
    abstract fun countryDao(): CountryDao
    abstract fun savedItemDao(): SavedItemDao
    abstract fun checklistProgressDao(): ChecklistProgressDao
}
