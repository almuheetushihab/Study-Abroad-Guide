package com.yourname.studyabroad.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val tier: String,
    val tuitionRangeText: String,
    val livingRangeText: String,
    val workRuleText: String,
    val postStudyText: String,
    val advisory: String?,
    val sourceUrl: String,
    val verifiedOnString: String,
)
