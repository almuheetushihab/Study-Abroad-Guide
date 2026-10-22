package com.shihab.studyabroad.data.mapper

import com.shihab.studyabroad.data.local.entity.ScholarshipEntity
import com.shihab.studyabroad.domain.model.Eligibility
import com.shihab.studyabroad.domain.model.FundingType
import com.shihab.studyabroad.domain.model.Scholarship
import kotlinx.datetime.LocalDate

fun ScholarshipEntity.toDomain(): Scholarship = Scholarship(
    id = id,
    name = name,
    countryId = countryId,
    level = levelCsv.split(",").map { it.trim() },
    funding = FundingType.valueOf(funding),
    openDate = LocalDate.parse(openDateString),
    deadline = LocalDate.parse(deadlineString),
    applyUrl = applyUrl,
    sourceUrl = sourceUrl,
    verifiedOn = LocalDate.parse(verifiedOnString),
    eligibility = Eligibility(
        minCgpa = minCgpa,
        minIelts = minIelts,
        minExpYears = minExpYears,
        maxAge = maxAge,
    ),
)

fun Scholarship.toEntity(): ScholarshipEntity = ScholarshipEntity(
    id = id,
    name = name,
    countryId = countryId,
    levelCsv = level.joinToString(","),
    funding = funding.name,
    openDateString = openDate.toString(),
    deadlineString = deadline.toString(),
    applyUrl = applyUrl,
    sourceUrl = sourceUrl,
    verifiedOnString = verifiedOn.toString(),
    minCgpa = eligibility.minCgpa,
    minIelts = eligibility.minIelts,
    minExpYears = eligibility.minExpYears,
    maxAge = eligibility.maxAge,
)
