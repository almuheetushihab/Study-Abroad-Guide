package com.shihab.studyabroad.data.repository

import com.shihab.studyabroad.domain.model.ChecklistItem
import com.shihab.studyabroad.domain.repository.ChecklistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeChecklistRepository @Inject constructor() : ChecklistRepository {

    private val checklistFlow = MutableStateFlow(SampleData.sampleChecklistItems)

    override fun getChecklistItems(): Flow<List<ChecklistItem>> = checklistFlow.asStateFlow()

    override suspend fun toggleItemCompleted(id: String) {
        checklistFlow.value = checklistFlow.value.map { item ->
            if (item.id == id) item.copy(isCompleted = !item.isCompleted) else item
        }
    }
}
