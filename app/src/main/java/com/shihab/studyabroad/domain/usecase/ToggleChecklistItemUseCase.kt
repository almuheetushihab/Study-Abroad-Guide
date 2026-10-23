package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.domain.repository.ChecklistRepository
import javax.inject.Inject

class ToggleChecklistItemUseCase @Inject constructor(private val checklistRepository: ChecklistRepository) {
    suspend operator fun invoke(itemId: String) {
        checklistRepository.toggleItemCompleted(itemId)
    }
}
