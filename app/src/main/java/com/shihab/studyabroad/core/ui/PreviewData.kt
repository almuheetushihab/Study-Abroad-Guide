package com.shihab.studyabroad.core.ui

import com.shihab.studyabroad.domain.model.ChecklistItem
import com.shihab.studyabroad.domain.model.Country
import com.shihab.studyabroad.domain.model.CountryTier
import com.shihab.studyabroad.domain.model.Eligibility
import com.shihab.studyabroad.domain.model.FundingGoal
import com.shihab.studyabroad.domain.model.FundingType
import com.shihab.studyabroad.domain.model.Scholarship
import com.shihab.studyabroad.domain.model.TestType
import com.shihab.studyabroad.domain.model.Track
import com.shihab.studyabroad.domain.model.University
import com.shihab.studyabroad.domain.model.UserProfile
import kotlinx.datetime.LocalDate

/**
 * Preview fixture data for Composable previews in feature modules.
 * Keeping sample data fixtures in core/ui preserves feature domain isolation.
 */
// SAMPLE_DATA_NOT_VERIFIED: The following datasets are placeholder sample data for preview purposes only.
const val SAMPLE_DATA_NOT_VERIFIED = "SAMPLE_DATA_NOT_VERIFIED"

object PreviewData {

    val sampleCountries = listOf(
        Country(
            id = "de",
            name = "Germany",
            tier = CountryTier.SCHOLARSHIP,
            tuitionRangeText = "0 - 1,500 EUR/year (Public Universities)",
            livingRangeText = "934 - 1,120 EUR/month",
            workRuleText = "120 full days or 240 half days per year",
            postStudyText = "18-month Job Seeking Visa after graduation",
            advisory = "Block account (Sperrkonto) required for visa.",
            sourceUrl = "https://www.daad.de",
            verifiedOn = LocalDate(2026, 9, 15),
        ),
        Country(
            id = "ca",
            name = "Canada",
            tier = CountryTier.PR_WORK,
            tuitionRangeText = "15,000 - 35,000 CAD/year",
            livingRangeText = "1,500 - 2,200 CAD/month",
            workRuleText = "20 hours per week off-campus during terms",
            postStudyText = "PGWP (Post-Graduation Work Permit) up to 3 years",
            advisory = "Provincial Attestation Letter (PAL) required for most undergrad applicants.",
            sourceUrl = "https://www.canada.ca/en/immigration-refugees-citizenship.html",
            verifiedOn = LocalDate(2026, 10, 1),
        ),
        Country(
            id = "jp",
            name = "Japan",
            tier = CountryTier.SCHOLARSHIP,
            tuitionRangeText = "535,800 JPY/year (National Universities)",
            livingRangeText = "100,000 - 150,000 JPY/month",
            workRuleText = "Up to 28 hours per week with permit",
            postStudyText = "Designated Activities Visa (1 year job hunt, extendable)",
            advisory = "Japanese language proficiency (JLPT N2/N1) expands career opportunities.",
            sourceUrl = "https://www.studyinjapan.go.jp",
            verifiedOn = LocalDate(2026, 9, 20),
        ),
    )

    val sampleScholarships = listOf(
        Scholarship(
            id = "daad-epos",
            name = "DAAD EPOS Scholarship",
            countryId = "de",
            level = listOf("MSc", "PhD"),
            funding = FundingType.FULL,
            openDate = LocalDate(2026, 8, 1),
            deadline = LocalDate(2026, 11, 30),
            applyUrl = "https://www.daad.de/epos",
            sourceUrl = "https://www.daad.de/epos",
            verifiedOn = LocalDate(2026, 10, 1),
            eligibility = Eligibility(
                minCgpa = 3.0,
                minIelts = 6.5,
                minExpYears = 2,
                maxAge = null,
            ),
        ),
        Scholarship(
            id = "stipendium-hungaricum",
            name = "Stipendium Hungaricum Scholarship",
            countryId = "hu",
            level = listOf("BSc", "MSc", "PhD"),
            funding = FundingType.FULL,
            openDate = LocalDate(2026, 11, 15),
            deadline = LocalDate(2027, 1, 15),
            applyUrl = "https://stipendiumhungaricum.hu/apply/",
            sourceUrl = "https://stipendiumhungaricum.hu/",
            verifiedOn = LocalDate(2026, 5, 15),
            eligibility = Eligibility(
                minCgpa = null,
                minIelts = null,
                minExpYears = null,
                maxAge = null,
            ),
        ),
    )

    val sampleUniversities = listOf(
        University(
            id = "tum",
            countryId = "de",
            name = "Technical University of Munich (TUM)",
            worldRank = 28,
            websiteUrl = "https://www.tum.de",
            verifiedOn = LocalDate(2026, 9, 15),
            level = listOf("BSc", "MSc"),
            programs = listOf("Computer Science", "Data Engineering & Analytics"),
            tuitionFeeText = "0 EUR / semester (Public)",
            deadline = LocalDate(2027, 5, 31),
            minCgpa = 3.2,
            minIelts = 6.5,
            minExpYears = 0,
            hasScholarship = true,
        ),
        University(
            id = "utoronto",
            countryId = "ca",
            name = "University of Toronto",
            worldRank = 21,
            websiteUrl = "https://www.utoronto.ca",
            verifiedOn = LocalDate(2026, 10, 1),
            level = listOf("BSc", "MSc", "PhD"),
            programs = listOf("Computer Science", "Software Engineering"),
            tuitionFeeText = "45,000 CAD / year",
            deadline = LocalDate(2027, 1, 15),
            minCgpa = 3.5,
            minIelts = 7.0,
            minExpYears = null,
            hasScholarship = true,
        ),
    )

    val sampleUserProfile = UserProfile(
        id = "user_1",
        name = "Rahim Ahmed",
        targetTrack = Track.GRADUATE,
        level = "Master",
        subject = "Computer Science",
        cgpa = 3.5,
        cgpaScale = 4.0,
        testType = TestType.IELTS,
        testScore = 7.0,
        experienceYears = 1,
        age = 24,
        budgetRange = "10-20 Lakh BDT",
        goal = FundingGoal.SCHOLARSHIP,
        targetCountries = listOf("de", "hu"),
    )

    val sampleChecklistItems = listOf(
        ChecklistItem(
            id = "chk_1",
            title = "Passport Validity Check",
            description = "Ensure passport has at least 2 years validity remaining.",
            isCompleted = true,
            category = "Identity",
        ),
        ChecklistItem(
            id = "chk_2",
            title = "Academic Transcripts Attestation",
            description = "Attest HSC and BSc transcripts from Education Board and Ministry of Foreign Affairs.",
            isCompleted = false,
            category = "Academics",
        ),
    )
}
