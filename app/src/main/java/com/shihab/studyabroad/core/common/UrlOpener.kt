package com.shihab.studyabroad.core.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interface abstraction for opening external URLs so ViewModel and Composables can be tested without Android Context.
 */
interface UrlOpener {
    fun openUrl(url: String)
}

@Singleton
class AndroidUrlOpener @Inject constructor(@ApplicationContext private val context: Context) : UrlOpener {
    override fun openUrl(url: String) {
        if (url.isBlank()) return
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            // Safe fallback for tests / environments without browser activity
        }
    }
}
