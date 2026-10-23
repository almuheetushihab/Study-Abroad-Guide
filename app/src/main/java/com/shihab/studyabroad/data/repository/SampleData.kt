package com.shihab.studyabroad.data.repository

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

// SAMPLE_DATA_NOT_VERIFIED: The following datasets are placeholder sample data for development and preview purposes only.
const val SAMPLE_DATA_NOT_VERIFIED = "SAMPLE_DATA_NOT_VERIFIED"
const val IS_SAMPLE_DATA_VERIFIED = false

object SampleData {

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
        Country(
            id = "hu",
            name = "Hungary",
            tier = CountryTier.SCHOLARSHIP,
            tuitionRangeText = "0 EUR (Stipendium) or 3,000 - 5,500 EUR/year",
            livingRangeText = "375 - 650 EUR/month",
            workRuleText = "30 hours per week during term time",
            postStudyText = "9 months Study-to-Work visa permit",
            advisory = null,
            sourceUrl = "https://stipendiumhungaricum.hu",
            verifiedOn = LocalDate(2026, 5, 1), // > 90 days relative to 2026-10-23 (Stale / May be outdated)
        ),
        Country(
            id = "tr",
            name = "Turkey",
            tier = CountryTier.SCHOLARSHIP,
            tuitionRangeText = "0 USD (Burslari) or 1,500 - 5,000 USD/year",
            livingRangeText = "300 - 550 USD/month",
            workRuleText = "Allowed with work permit for postgrads",
            postStudyText = "1 year job search permit after graduation",
            advisory = null,
            sourceUrl = "https://turkiyeburslari.gov.tr",
            verifiedOn = LocalDate(2026, 10, 5),
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
            deadline = LocalDate(2026, 11, 30), // OPEN relative to 2026-10-23
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
            id = "adb-japan",
            name = "ADB-Japan Scholarship Program",
            countryId = "jp",
            level = listOf("MSc"),
            funding = FundingType.FULL,
            openDate = LocalDate(2026, 9, 1),
            deadline = LocalDate(2026, 12, 15), // OPEN relative to 2026-10-23
            applyUrl = "https://www.adb.org/work-with-us/careers/japan-scholarship-program",
            sourceUrl = "https://www.adb.org",
            verifiedOn = LocalDate(2026, 9, 20),
            eligibility = Eligibility(
                minCgpa = 3.0,
                minIelts = 6.0,
                minExpYears = 2,
                maxAge = 45,
            ),
        ),
        Scholarship(
            id = "vanier-cgs",
            name = "Vanier Canada Graduate Scholarships",
            countryId = "ca",
            level = listOf("PhD"),
            funding = FundingType.FULL,
            openDate = LocalDate(2026, 7, 1),
            deadline = LocalDate(2026, 11, 1), // OPEN relative to 2026-10-23
            applyUrl = "https://vanier.gc.ca",
            sourceUrl = "https://vanier.gc.ca",
            verifiedOn = LocalDate(2026, 9, 10),
            eligibility = Eligibility(
                minCgpa = 3.7,
                minIelts = 7.0,
                minExpYears = null,
                maxAge = null,
            ),
        ),
        Scholarship(
            id = "lester-b-pearson",
            name = "Lester B. Pearson International Scholarship",
            countryId = "ca",
            level = listOf("BSc"),
            funding = FundingType.FULL,
            openDate = LocalDate(2026, 9, 15),
            deadline = LocalDate(2026, 11, 15), // OPEN relative to 2026-10-23
            applyUrl = "https://future.utoronto.ca/pearson",
            sourceUrl = "https://future.utoronto.ca/pearson",
            verifiedOn = LocalDate(2026, 10, 5),
            eligibility = Eligibility(
                minCgpa = 3.8,
                minIelts = 7.5,
                minExpYears = 0,
                maxAge = 21,
            ),
        ),
        Scholarship(
            id = "stipendium-hungaricum",
            name = "Stipendium Hungaricum Scholarship",
            countryId = "hu",
            level = listOf("BSc", "MSc", "PhD"),
            funding = FundingType.FULL,
            openDate = LocalDate(2026, 11, 15), // OPENING_SOON relative to 2026-10-23
            deadline = LocalDate(2027, 1, 15),
            applyUrl = "https://stipendiumhungaricum.hu/apply/",
            sourceUrl = "https://stipendiumhungaricum.hu/",
            verifiedOn = LocalDate(2026, 5, 15), // > 90 days relative to 2026-10-23 (Stale / May be outdated)
            eligibility = Eligibility(
                minCgpa = null, // Empty eligibility field -> "Check the official site"
                minIelts = null,
                minExpYears = null,
                maxAge = null,
            ),
        ),
        Scholarship(
            id = "turkiye-burslari",
            name = "Türkiye Burslari Scholarship",
            countryId = "tr",
            level = listOf("BSc", "MSc", "PhD"),
            funding = FundingType.FULL,
            openDate = LocalDate(2026, 12, 1), // OPENING_SOON relative to 2026-10-23
            deadline = LocalDate(2027, 2, 20),
            applyUrl = "https://turkiyeburslari.gov.tr",
            sourceUrl = "https://turkiyeburslari.gov.tr",
            verifiedOn = LocalDate(2026, 10, 10),
            eligibility = Eligibility(
                minCgpa = 3.0,
                minIelts = 6.0,
                minExpYears = 0,
                maxAge = 30,
            ),
        ),
        Scholarship(
            id = "mext-embassy",
            name = "MEXT Embassy Recommendation Scholarship",
            countryId = "jp",
            level = listOf("BSc", "MSc", "PhD"),
            funding = FundingType.FULL,
            openDate = LocalDate(2026, 4, 15),
            deadline = LocalDate(2026, 5, 20), // CLOSED relative to 2026-10-23 -> Expected next cycle: 2027
            applyUrl = "https://www.bd.emb-japan.go.jp",
            sourceUrl = "https://www.bd.emb-japan.go.jp",
            verifiedOn = LocalDate(2026, 4, 1),
            eligibility = Eligibility(
                minCgpa = 3.25,
                minIelts = 6.0,
                minExpYears = 0,
                maxAge = 35,
            ),
        ),
        Scholarship(
            id = "deutschlandstipendium",
            name = "Deutschlandstipendium National Scholarship",
            countryId = "de",
            level = listOf("BSc", "MSc"),
            funding = FundingType.PARTIAL,
            openDate = LocalDate(2026, 6, 1),
            deadline = LocalDate(2026, 7, 15), // CLOSED relative to 2026-10-23 -> Expected next cycle: 2027
            applyUrl = "https://www.deutschlandstipendium.de",
            sourceUrl = "https://www.deutschlandstipendium.de",
            verifiedOn = LocalDate(2026, 5, 20),
            eligibility = Eligibility(
                minCgpa = 3.3,
                minIelts = 6.5,
                minExpYears = 0,
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
            programs = listOf("Computer Science", "Data Engineering & Analytics", "Robotics"),
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
            programs = listOf("Computer Science", "Software Engineering", "Artificial Intelligence"),
            tuitionFeeText = "45,000 CAD / year",
            deadline = LocalDate(2027, 1, 15),
            minCgpa = 3.5,
            minIelts = 7.0,
            minExpYears = null,
            hasScholarship = true,
        ),
        University(
            id = "utokyo",
            countryId = "jp",
            name = "The University of Tokyo",
            worldRank = 28,
            websiteUrl = "https://www.u-tokyo.ac.jp",
            verifiedOn = LocalDate(2026, 9, 20),
            level = listOf("BSc", "MSc", "PhD"),
            programs = listOf("Information Science", "Civil Engineering", "Physics"),
            tuitionFeeText = "535,800 JPY / year",
            deadline = LocalDate(2027, 4, 30),
            minCgpa = 3.3,
            minIelts = 6.5,
            minExpYears = 0,
            hasScholarship = true,
        ),
        University(
            id = "elte",
            countryId = "hu",
            name = "Eötvös Loránd University (ELTE)",
            worldRank = 500,
            websiteUrl = "https://www.elte.hu/en",
            verifiedOn = LocalDate(2026, 5, 10), // > 90 days relative to 2026-10-23 (Stale / May be outdated)
            level = listOf("BSc", "MSc"),
            programs = listOf("Computer Science", "Cartography", "Software Technology"),
            tuitionFeeText = "3,500 EUR / year",
            deadline = LocalDate(2027, 1, 15),
            minCgpa = 2.8,
            minIelts = 5.5,
            minExpYears = 0,
            hasScholarship = true,
        ),
        University(
            id = "metu",
            countryId = "tr",
            name = "Middle East Technical University (METU)",
            worldRank = 336,
            websiteUrl = "https://www.metu.edu.tr",
            verifiedOn = LocalDate(2026, 9, 25),
            level = listOf("BSc", "MSc", "PhD"),
            programs = listOf("Electrical Engineering", "Computer Engineering", "Aerospace"),
            tuitionFeeText = "2,000 USD / year",
            deadline = LocalDate(2027, 6, 30),
            minCgpa = null, // Missing requirement -> Requirement meter shows "Unknown"
            minIelts = null,
            minExpYears = null,
            hasScholarship = true,
        ),
        University(
            id = "ubc",
            countryId = "ca",
            name = "University of British Columbia (UBC)",
            worldRank = 34,
            websiteUrl = "https://www.ubc.ca",
            verifiedOn = LocalDate(2026, 10, 2),
            level = listOf("BSc", "MSc"),
            programs = listOf("Data Science", "Business Administration", "Biomedical Engineering"),
            tuitionFeeText = "38,000 CAD / year",
            deadline = LocalDate(2027, 1, 31),
            minCgpa = 3.4,
            minIelts = 6.5,
            minExpYears = 1,
            hasScholarship = false,
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
        ChecklistItem(
            id = "chk_3",
            title = "IELTS Academic Test",
            description = "Target overall score 7.0 with no band less than 6.5.",
            isCompleted = true,
            category = "Language",
        ),
        ChecklistItem(
            id = "chk_4",
            title = "Statement of Purpose (SOP)",
            description = "Draft 2-page SOP aligned with research track and career goals.",
            isCompleted = false,
            category = "Documents",
        ),
    )
}
