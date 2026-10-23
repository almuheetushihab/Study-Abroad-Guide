package com.shihab.studyabroad.domain.usecase

import com.shihab.studyabroad.domain.model.FundingGoal
import com.shihab.studyabroad.domain.model.RequirementStatus
import com.shihab.studyabroad.domain.model.TestType
import com.shihab.studyabroad.domain.model.Track
import com.shihab.studyabroad.domain.model.University
import com.shihab.studyabroad.domain.model.UserProfile
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class CompareRequirementUseCaseTest {

    private val useCase = CompareRequirementUseCase()

    private val sampleUniversity = University(
        id = "tum",
        countryId = "de",
        name = "Technical University of Munich",
        worldRank = 28,
        websiteUrl = "https://www.tum.de",
        verifiedOn = LocalDate(2025, 1, 10),
        minCgpa = 3.2,
        minIelts = 6.5,
        minExpYears = 2,
    )

    private val sampleProfile = UserProfile(
        id = "user1",
        name = "Test User",
        targetTrack = Track.GRADUATE,
        level = "Master",
        subject = "CSE",
        cgpa = 3.5,
        cgpaScale = 4.0,
        testType = TestType.IELTS,
        testScore = 7.0,
        experienceYears = 3,
        age = 25,
        budgetRange = "10-20 Lakh BDT",
        goal = FundingGoal.SCHOLARSHIP,
        targetCountries = listOf("de"),
    )

    @Test
    fun `compareCgpa returns OK when user CGPA is greater than or equal to required`() {
        val status = useCase.compareCgpa(requiredMinCgpa = 3.2, userCgpa = 3.5)
        assertEquals(RequirementStatus.OK, status)
    }

    @Test
    fun `compareCgpa returns LOW when user CGPA is below required`() {
        val status = useCase.compareCgpa(requiredMinCgpa = 3.2, userCgpa = 3.0)
        assertEquals(RequirementStatus.LOW, status)
    }

    @Test
    fun `compareCgpa returns UNKNOWN when requirement or user CGPA is null`() {
        assertEquals(RequirementStatus.UNKNOWN, useCase.compareCgpa(requiredMinCgpa = null, userCgpa = 3.5))
        assertEquals(RequirementStatus.UNKNOWN, useCase.compareCgpa(requiredMinCgpa = 3.2, userCgpa = null))
    }

    @Test
    fun `compareTestScore returns OK when user score is equal or above requirement`() {
        val status = useCase.compareTestScore(requiredMinIelts = 6.5, userScore = 7.0)
        assertEquals(RequirementStatus.OK, status)
    }

    @Test
    fun `compareTestScore returns LOW when user score is below requirement`() {
        val status = useCase.compareTestScore(requiredMinIelts = 7.0, userScore = 6.0)
        assertEquals(RequirementStatus.LOW, status)
    }

    @Test
    fun `compareTestScore returns UNKNOWN when requirement is null`() {
        val status = useCase.compareTestScore(requiredMinIelts = null, userScore = 7.0)
        assertEquals(RequirementStatus.UNKNOWN, status)
    }

    @Test
    fun `invoke evaluates full profile correctly against university requirements`() {
        val result = useCase(sampleUniversity, sampleProfile)
        assertEquals(RequirementStatus.OK, result.cgpaStatus)
        assertEquals(RequirementStatus.OK, result.testStatus)
        assertEquals(RequirementStatus.OK, result.expStatus)
    }

    @Test
    fun `invoke returns UNKNOWN for missing university requirements`() {
        val universityMissingReqs = sampleUniversity.copy(minCgpa = null, minIelts = null, minExpYears = null)
        val result = useCase(universityMissingReqs, sampleProfile)
        assertEquals(RequirementStatus.UNKNOWN, result.cgpaStatus)
        assertEquals(RequirementStatus.UNKNOWN, result.testStatus)
        assertEquals(RequirementStatus.UNKNOWN, result.expStatus)
    }
}
