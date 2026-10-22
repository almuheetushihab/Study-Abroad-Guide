package com.shihab.studyabroad

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity hosting the Study Abroad Guide application UI.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyAbroadTheme {
                AppNavHost()
            }
        }
    }
}
