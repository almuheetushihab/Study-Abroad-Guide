package com.yourname.studyabroad.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scholarships")
data class ScholarshipEntity(
    @PrimaryKey val id: String,
    val name: String,
    val countryId: String,
    val levelCsv: String,
    val funding: String,
    val openDateString: String,
    val deadlineString: String,
    val applyUrl: String,
    val sourceUrl: String,
    val verifiedOnString: String,
    val minCgpa: Double?,
    val minIelts: Double?,
    val minExpYears: Int?,
    val maxAge: Int?,
)
