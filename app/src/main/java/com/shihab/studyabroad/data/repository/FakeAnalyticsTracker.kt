package com.shihab.studyabroad.data.repository

import com.shihab.studyabroad.core.common.AnalyticsTracker
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAnalyticsTracker @Inject constructor() : AnalyticsTracker {
    val loggedEvents = mutableListOf<Pair<String, Map<String, Any>>>()

    override fun logEvent(name: String, params: Map<String, Any>) {
        loggedEvents.add(name to params)
    }
}
