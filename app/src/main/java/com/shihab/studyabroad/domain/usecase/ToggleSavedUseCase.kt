package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.domain.model.SavedType
import com.shihab.studyabroad.domain.repository.SavedRepository
import javax.inject.Inject

class ToggleSavedUseCase @Inject constructor(private val savedRepository: SavedRepository) {
    suspend operator fun invoke(type: SavedType, itemId: String) {
        savedRepository.toggleSaved(type, itemId)
    }
}
