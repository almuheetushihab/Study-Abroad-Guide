package com.shihab.studyabroad.core.common

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

/**
 * Manager for per-app language switching using AppCompatDelegate.
 */
object LanguageManager {

    /**
     * Sets the application locale to the given language code (e.g. "en" or "bn").
     */
    fun setLanguage(languageCode: String) {
        val appLocale = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    /**
     * Returns the current active language code ("en" or "bn").
     */
    fun getCurrentLanguage(): String {
        val locales = AppCompatDelegate.getApplicationLocales()
        if (!locales.isEmpty) {
            return locales.get(0)?.language?.ifEmpty { "en" } ?: "en"
        }
        return Locale.getDefault().language.ifEmpty { "en" }
    }
}
