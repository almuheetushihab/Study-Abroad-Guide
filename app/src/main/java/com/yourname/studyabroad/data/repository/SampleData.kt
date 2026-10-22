package com.yourname.studyabroad.data.repository

import com.yourname.studyabroad.domain.model.ChecklistItem
import com.yourname.studyabroad.domain.model.Country
import com.yourname.studyabroad.domain.model.CountryTier
import com.yourname.studyabroad.domain.model.Eligibility
import com.yourname.studyabroad.domain.model.FundingType
import com.yourname.studyabroad.domain.model.Scholarship
import com.yourname.studyabroad.domain.model.Track
import com.yourname.studyabroad.domain.model.University
import com.yourname.studyabroad.domain.model.UserProfile
import kotlinx.datetime.LocalDate

// SAMPLE_DATA_NOT_VERIFIED: The following datasets are placeholder sample data for development and preview purposes only.
const val SAMPLE_DATA_NOT_VERIFIED = "SAMPLE_DATA_NOT_VERIFIED"
const val IS_SAMPLE_DATA_VERIFIED = false

object SampleData {
    val sampleCountries =
        listOf(
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
                verifiedOn = LocalDate(2025, 1, 15),
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
                verifiedOn = LocalDate(2025, 2, 1),
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
                verifiedOn = LocalDate(2025, 1, 20),
            ),
        )

    val sampleScholarships =
        listOf(
            Scholarship(
                id = "daad-epos",
                name = "DAAD EPOS Scholarship",
                countryId = "de",
                level = listOf("MSc", "PhD"),
                funding = FundingType.FULL,
                openDate = LocalDate(2025, 8, 1),
                deadline = LocalDate(2025, 11, 30),
                applyUrl = "https://www.daad.de/epos",
                sourceUrl = "https://www.daad.de/epos",
                verifiedOn = LocalDate(2025, 1, 10),
                eligibility =
                Eligibility(
                    minCgpa = 3.0,
                    minIelts = 6.5,
                    minExpYears = 2,
                    maxAge = null,
                ),
            ),
            Scholarship(
                id = "mext-embassy",
                name = "MEXT Embassy Recommendation Scholarship",
                countryId = "jp",
                level = listOf("BSc", "MSc", "PhD"),
                funding = FundingType.FULL,
                openDate = LocalDate(2025, 4, 15),
                deadline = LocalDate(2025, 5, 20),
                applyUrl = "https://www.bd.emb-japan.go.jp",
                sourceUrl = "https://www.bd.emb-japan.go.jp",
                verifiedOn = LocalDate(2025, 2, 5),
                eligibility =
                Eligibility(
                    minCgpa = 3.25,
                    minIelts = 6.0,
                    minExpYears = 0,
                    maxAge = 35,
                ),
            ),
            Scholarship(
                id = "vanier-cgs",
                name = "Vanier Canada Graduate Scholarships",
                countryId = "ca",
                level = listOf("PhD"),
                funding = FundingType.FULL,
                openDate = LocalDate(2025, 7, 1),
                deadline = LocalDate(2025, 11, 1),
                applyUrl = "https://vanier.gc.ca",
                sourceUrl = "https://vanier.gc.ca",
                verifiedOn = LocalDate(2025, 1, 5),
                eligibility =
                Eligibility(
                    minCgpa = 3.7,
                    minIelts = 7.0,
                    minExpYears = null,
                    maxAge = null,
                ),
            ),
            Scholarship(
                id = "deutschlandstipendium",
                name = "Deutschlandstipendium National Scholarship",
                countryId = "de",
                level = listOf("BSc", "MSc"),
                funding = FundingType.PARTIAL,
                openDate = LocalDate(2025, 6, 1),
                deadline = LocalDate(2025, 7, 15),
                applyUrl = "https://www.deutschlandstipendium.de",
                sourceUrl = "https://www.deutschlandstipendium.de",
                verifiedOn = LocalDate(2025, 1, 20),
                eligibility =
                Eligibility(
                    minCgpa = 3.3,
                    minIelts = 6.5,
                    minExpYears = 0,
                    maxAge = null,
                ),
            ),
            Scholarship(
                id = "adb-japan",
                name = "ADB-Japan Scholarship Program",
                countryId = "jp",
                level = listOf("MSc"),
                funding = FundingType.FULL,
                openDate = LocalDate(2025, 9, 1),
                deadline = LocalDate(2025, 12, 15),
                applyUrl = "https://www.adb.org/work-with-us/careers/japan-scholarship-program",
                sourceUrl = "https://www.adb.org",
                verifiedOn = LocalDate(2025, 2, 1),
                eligibility =
                Eligibility(
                    minCgpa = 3.0,
                    minIelts = 6.0,
                    minExpYears = 2,
                    maxAge = 45,
                ),
            ),
        )

    val sampleUniversities =
        listOf(
            University(
                id = "tum",
                countryId = "de",
                name = "Technical University of Munich (TUM)",
                worldRank = 28,
                websiteUrl = "https://www.tum.de",
                verifiedOn = LocalDate(2025, 1, 10),
            ),
            University(
                id = "utoronto",
                countryId = "ca",
                name = "University of Toronto",
                worldRank = 21,
                websiteUrl = "https://www.utoronto.ca",
                verifiedOn = LocalDate(2025, 1, 10),
            ),
            University(
                id = "utokyo",
                countryId = "jp",
                name = "The University of Tokyo",
                worldRank = 28,
                websiteUrl = "https://www.u-tokyo.ac.jp",
                verifiedOn = LocalDate(2025, 1, 10),
            ),
        )

    val sampleUserProfile =
        UserProfile(
            id = "user_1",
            name = "Rahim Ahmed",
            targetTrack = Track.GRADUATE,
            cgpa = 3.5,
            ieltsScore = 7.0,
            experienceYears = 1,
        )

    val sampleChecklistItems =
        listOf(
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
