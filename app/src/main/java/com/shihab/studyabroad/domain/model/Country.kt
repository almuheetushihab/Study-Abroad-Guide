package com.shihab.studyabroad.domain.model

import kotlinx.datetime.LocalDate

data class Country(
    val id: String,
    val name: String,
    val tier: CountryTier,
    val tuitionRangeText: String,
    val livingRangeText: String,
    val workRuleText: String,
    val postStudyText: String,
    val advisory: String?,
    val sourceUrl: String,
    val verifiedOn: LocalDate,
)
