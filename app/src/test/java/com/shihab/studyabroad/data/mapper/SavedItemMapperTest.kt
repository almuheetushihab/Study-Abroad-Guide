package com.shihab.studyabroad.data.mapper

import com.shihab.studyabroad.data.local.entity.SavedItemEntity
import com.shihab.studyabroad.domain.model.SavedItem
import com.shihab.studyabroad.domain.model.SavedType
import org.junit.Assert.assertEquals
import org.junit.Test

class SavedItemMapperTest {

    @Test
    fun `SavedItemEntity toDomain maps correctly`() {
        val entity = SavedItemEntity(
            type = "SCHOLARSHIP",
            itemId = "daad-epos",
            savedAt = 123456789L,
        )

        val domain = entity.toDomain()

        assertEquals(SavedType.SCHOLARSHIP, domain.type)
        assertEquals("daad-epos", domain.itemId)
        assertEquals(123456789L, domain.savedAt)
    }

    @Test
    fun `SavedItem toEntity maps correctly`() {
        val domain = SavedItem(
            type = SavedType.COUNTRY,
            itemId = "de",
            savedAt = 987654321L,
        )

        val entity = domain.toEntity()

        assertEquals("COUNTRY", entity.type)
        assertEquals("de", entity.itemId)
        assertEquals(987654321L, entity.savedAt)
    }
}
