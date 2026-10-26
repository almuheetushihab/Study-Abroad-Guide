package com.shihab.studyabroad.data.repository

import android.app.Activity
import com.shihab.studyabroad.core.common.PhoneAuthLauncher
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidPhoneAuthLauncher @Inject constructor() : PhoneAuthLauncher {
    private var activityRef: WeakReference<Activity>? = null

    override fun registerActivity(activity: Activity?) {
        activityRef = if (activity != null) WeakReference(activity) else null
    }

    override fun getActivity(): Activity? = activityRef?.get()
}
