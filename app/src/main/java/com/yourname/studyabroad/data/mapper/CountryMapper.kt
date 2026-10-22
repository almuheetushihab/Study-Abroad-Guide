package com.yourname.studyabroad.data.mapper

import com.yourname.studyabroad.data.local.entity.CountryEntity
import com.yourname.studyabroad.domain.model.Country
import com.yourname.studyabroad.domain.model.CountryTier
import kotlinx.datetime.LocalDate

fun CountryEntity.toDomain(): Country = Country(
    id = id,
    name = name,
    tier = CountryTier.valueOf(tier),
    tuitionRangeText = tuitionRangeText,
    livingRangeText = livingRangeText,
    workRuleText = workRuleText,
    postStudyText = postStudyText,
    advisory = advisory,
    sourceUrl = sourceUrl,
    verifiedOn = LocalDate.parse(verifiedOnString),
)

fun Country.toEntity(): CountryEntity = CountryEntity(
    id = id,
    name = name,
    tier = tier.name,
    tuitionRangeText = tuitionRangeText,
    livingRangeText = livingRangeText,
    workRuleText = workRuleText,
    postStudyText = postStudyText,
    advisory = advisory,
    sourceUrl = sourceUrl,
    verifiedOnString = verifiedOn.toString(),
)
