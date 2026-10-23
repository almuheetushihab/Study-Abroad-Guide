package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.domain.model.RequirementStatus
import com.shihab.studyabroad.domain.model.University
import com.shihab.studyabroad.domain.model.UserProfile

data class RequirementComparisonResult(
    val cgpaStatus: RequirementStatus,
    val testStatus: RequirementStatus,
    val expStatus: RequirementStatus,
)

/**
 * Requirement vs Profile evaluation meter (FR-UNI-003).
 * Returns OK, Low or Unknown.
 * CRITICAL RULE: If a requirement is missing or user profile data is missing,
 * it MUST return UNKNOWN, never a guess.
 */
class CompareRequirementUseCase {

    operator fun invoke(university: University, userProfile: UserProfile?): RequirementComparisonResult {
        val cgpaStatus = compareCgpa(university.minCgpa, userProfile?.cgpa)
        val testStatus = compareTestScore(university.minIelts, userProfile?.testScore)
        val expStatus = compareExperience(university.minExpYears, userProfile?.experienceYears)

        return RequirementComparisonResult(
            cgpaStatus = cgpaStatus,
            testStatus = testStatus,
            expStatus = expStatus,
        )
    }

    fun compareCgpa(requiredMinCgpa: Double?, userCgpa: Double?): RequirementStatus {
        if (requiredMinCgpa == null || userCgpa == null) return RequirementStatus.UNKNOWN
        return if (userCgpa >= requiredMinCgpa) RequirementStatus.OK else RequirementStatus.LOW
    }

    fun compareTestScore(requiredMinIelts: Double?, userScore: Double?): RequirementStatus {
        if (requiredMinIelts == null || userScore == null) return RequirementStatus.UNKNOWN
        return if (userScore >= requiredMinIelts) RequirementStatus.OK else RequirementStatus.LOW
    }

    fun compareExperience(requiredMinExp: Int?, userExp: Int?): RequirementStatus {
        if (requiredMinExp == null || userExp == null) return RequirementStatus.UNKNOWN
        return if (userExp >= requiredMinExp) RequirementStatus.OK else RequirementStatus.LOW
    }
}
