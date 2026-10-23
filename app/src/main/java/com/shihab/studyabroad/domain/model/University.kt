package com.shihab.studyabroad.domain.model

import kotlinx.datetime.LocalDate

data class University(
    val id: String,
    val countryId: String,
    val name: String,
    val worldRank: Int?,
    val websiteUrl: String,
    val verifiedOn: LocalDate,
    val level: List<String> = listOf("BSc", "MSc"),
    val programs: List<String> = emptyList(),
    val tuitionFeeText: String? = null,
    val deadline: LocalDate? = null,
    val minCgpa: Double? = null,
    val minIelts: Double? = null,
    val minExpYears: Int? = null,
    val hasScholarship: Boolean = true,
    val sourceUrl: String = websiteUrl,
)
