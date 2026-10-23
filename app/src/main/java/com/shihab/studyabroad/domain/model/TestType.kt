package com.shihab.studyabroad.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class TestType {
    NONE,
    IELTS,
    TOEFL,
    PTE,
    DUOLINGO,
}
