package com.shihab.studyabroad.core.common

interface CrashReporter {
    fun log(message: String)
    fun recordNonFatal(throwable: Throwable)
    fun setCustomKey(key: String, value: String)
}
