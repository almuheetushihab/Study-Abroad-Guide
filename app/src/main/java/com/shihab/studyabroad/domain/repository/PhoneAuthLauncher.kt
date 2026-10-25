package com.shihab.studyabroad.domain.repository

import android.app.Activity

interface PhoneAuthLauncher {
    fun registerActivity(activity: Activity?)
    fun getActivity(): Activity?
}
