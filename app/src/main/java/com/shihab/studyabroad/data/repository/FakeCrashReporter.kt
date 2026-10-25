package com.shihab.studyabroad.data.repository

import com.shihab.studyabroad.core.common.CrashReporter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeCrashReporter @Inject constructor() : CrashReporter {
    override fun log(message: String) {}
    override fun recordNonFatal(throwable: Throwable) {}
    override fun setCustomKey(key: String, value: String) {}
}
