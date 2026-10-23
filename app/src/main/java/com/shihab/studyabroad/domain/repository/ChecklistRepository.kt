package com.shihab.studyabroad.domain.repository

import com.shihab.studyabroad.domain.model.ChecklistItem
import com.shihab.studyabroad.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface ChecklistRepository {
    fun getChecklistItems(track: Track? = null): Flow<List<ChecklistItem>>
    suspend fun toggleItemCompleted(id: String)
}
