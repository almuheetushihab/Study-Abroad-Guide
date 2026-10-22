package com.yourname.studyabroad.domain.repository

import com.yourname.studyabroad.domain.model.ChecklistItem
import kotlinx.coroutines.flow.Flow

interface ChecklistRepository {
    fun getChecklistItems(): Flow<List<ChecklistItem>>

    suspend fun toggleItemCompleted(id: String)
}
