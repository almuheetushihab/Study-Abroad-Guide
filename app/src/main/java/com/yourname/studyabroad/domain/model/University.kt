package com.yourname.studyabroad.domain.model

import kotlinx.datetime.LocalDate

data class University(
    val id: String,
    val countryId: String,
    val name: String,
    val worldRank: Int?,
    val websiteUrl: String,
    val verifiedOn: LocalDate,
)
