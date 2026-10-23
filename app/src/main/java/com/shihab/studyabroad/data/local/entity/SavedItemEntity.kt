package com.shihab.studyabroad.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "saved_items",
    primaryKeys = ["type", "itemId"],
)
data class SavedItemEntity(val type: String, val itemId: String, val savedAt: Long)
