package com.shihab.studyabroad.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist_progress")
data class ChecklistProgressEntity(@PrimaryKey val itemId: String, val isChecked: Boolean, val updatedAt: Long)
