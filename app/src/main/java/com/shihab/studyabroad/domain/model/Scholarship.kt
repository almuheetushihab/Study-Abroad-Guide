package com.shihab.studyabroad.domain.model

import kotlinx.datetime.LocalDate

data class Scholarship(
    val id: String,
    val name: String,
    val countryId: String,
    val level: List<String>,
    val funding: FundingType,
    val openDate: LocalDate,
    val deadline: LocalDate,
    val applyUrl: String,
    val sourceUrl: String,
    val verifiedOn: LocalDate,
    val eligibility: Eligibility,
)
