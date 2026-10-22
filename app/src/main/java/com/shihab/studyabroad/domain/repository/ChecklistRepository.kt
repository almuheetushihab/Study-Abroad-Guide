package com.shihab.studyabroad.domain.repository

import com.shihab.studyabroad.domain.model.ChecklistItem
import kotlinx.coroutines.flow.Flow

interface ChecklistRepository {
    fun getChecklistItems(): Flow<List<ChecklistItem>>
    suspend fun toggleItemCompleted(id: String)
}
