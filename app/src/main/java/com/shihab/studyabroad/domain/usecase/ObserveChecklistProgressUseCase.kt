package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.domain.model.ChecklistItem
import com.shihab.studyabroad.domain.repository.ChecklistRepository
import com.shihab.studyabroad.domain.repository.UserProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class ChecklistProgressData(
    val items: List<ChecklistItem> = emptyList(),
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val progressPercentage: Int = 0,
)

class ObserveChecklistProgressUseCase @Inject constructor(
    private val checklistRepository: ChecklistRepository,
    private val userProfileRepository: UserProfileRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<ChecklistProgressData> = userProfileRepository.getUserProfile().flatMapLatest { profile ->
        checklistRepository.getChecklistItems(profile?.targetTrack).map { items ->
            val completed = items.count { it.isCompleted }
            val total = items.size
            val percentage = if (total > 0) ((completed.toFloat() / total.toFloat()) * 100).toInt() else 0
            ChecklistProgressData(
                items = items,
                completedCount = completed,
                totalCount = total,
                progressPercentage = percentage,
            )
        }
    }
}
