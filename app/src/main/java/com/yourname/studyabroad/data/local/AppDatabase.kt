package com.yourname.studyabroad.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yourname.studyabroad.data.local.dao.CountryDao
import com.yourname.studyabroad.data.local.dao.ScholarshipDao
import com.yourname.studyabroad.data.local.entity.CountryEntity
import com.yourname.studyabroad.data.local.entity.ScholarshipEntity

@Database(
    entities = [ScholarshipEntity::class, CountryEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scholarshipDao(): ScholarshipDao

    abstract fun countryDao(): CountryDao
}
