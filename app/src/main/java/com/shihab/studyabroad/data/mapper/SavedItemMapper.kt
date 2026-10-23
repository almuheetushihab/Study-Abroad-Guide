package com.shihab.studyabroad.data.mapper

import com.shihab.studyabroad.data.local.entity.SavedItemEntity
import com.shihab.studyabroad.domain.model.SavedItem
import com.shihab.studyabroad.domain.model.SavedType

fun SavedItemEntity.toDomain(): SavedItem = SavedItem(
    type = SavedType.valueOf(type),
    itemId = itemId,
    savedAt = savedAt,
)

fun SavedItem.toEntity(): SavedItemEntity = SavedItemEntity(
    type = type.name,
    itemId = itemId,
    savedAt = savedAt,
)
