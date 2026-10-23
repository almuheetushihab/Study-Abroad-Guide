package com.shihab.studyabroad.feature.saved.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.core.designsystem.component.SectionHeader

@Composable
fun SavedGroupSection(title: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(modifier = modifier) {
        SectionHeader(title = title)
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}
