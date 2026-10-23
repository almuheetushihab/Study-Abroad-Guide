package com.shihab.studyabroad.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Track {
    FRESHER,
    GRADUATE,
    PROFESSIONAL,
    RESEARCHER,
}
