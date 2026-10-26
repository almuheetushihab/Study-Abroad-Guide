package com.shihab.studyabroad.domain.model

enum class ReportRefType {
    COUNTRY,
    UNIVERSITY,
    SCHOLARSHIP,
}

data class Report(val id: String = "", val refType: ReportRefType, val refId: String, val message: String, val createdAt: Long = 0L)
