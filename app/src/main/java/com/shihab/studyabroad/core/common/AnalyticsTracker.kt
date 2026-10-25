package com.shihab.studyabroad.core.common

interface AnalyticsTracker {
    fun logEvent(name: String, params: Map<String, Any> = emptyMap())
}
