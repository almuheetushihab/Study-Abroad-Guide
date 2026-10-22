package com.shihab.studyabroad.feature.track

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.shihab.studyabroad.R
import com.shihab.studyabroad.feature.documents.DocumentsScreen
import com.shihab.studyabroad.feature.saved.SavedScreen

@Composable
fun TrackScreen(modifier: Modifier = Modifier) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(id = R.string.track_tab_checklist),
        stringResource(id = R.string.track_tab_saved),
    )

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                )
            }
        }

        when (selectedTabIndex) {
            0 -> DocumentsScreen(modifier = Modifier.padding())
            1 -> SavedScreen(modifier = Modifier.padding())
        }
    }
}
