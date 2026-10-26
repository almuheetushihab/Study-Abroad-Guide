package com.shihab.studyabroad.core.common

import android.app.Activity

interface PhoneAuthLauncher {
    fun registerActivity(activity: Activity?)
    fun getActivity(): Activity?
}
