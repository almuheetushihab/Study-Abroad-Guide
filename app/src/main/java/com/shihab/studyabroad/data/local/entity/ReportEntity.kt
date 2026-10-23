package com.shihab.studyabroad.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity(@PrimaryKey val id: String, val refType: String, val refId: String, val message: String, val createdAt: Long)
