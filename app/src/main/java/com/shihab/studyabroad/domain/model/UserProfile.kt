package com.shihab.studyabroad.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String = "user_1",
    val name: String = "",
    val targetTrack: Track = Track.GRADUATE,
    val level: String = "",
    val subject: String = "",
    val cgpa: Double? = null,
    val cgpaScale: Double = 4.0,
    val testType: TestType = TestType.NONE,
    val testScore: Double? = null,
    val experienceYears: Int = 0,
    val age: Int? = null,
    val budgetRange: String = "",
    val goal: FundingGoal = FundingGoal.SCHOLARSHIP,
    val targetCountries: List<String> = emptyList(),
)
