package com.yourname.studyabroad.domain.model

data class UserProfile(
    val id: String,
    val name: String,
    val targetTrack: Track,
    val cgpa: Double?,
    val ieltsScore: Double?,
    val experienceYears: Int,
)
