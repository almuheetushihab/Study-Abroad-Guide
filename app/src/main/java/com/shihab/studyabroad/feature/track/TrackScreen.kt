package com.shihab.studyabroad.feature.track

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
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
import com.shihab.studyabroad.feature.documents.DocumentsRoute
import com.shihab.studyabroad.feature.saved.SavedRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackScreen(
    modifier: Modifier = Modifier,
    onNavigateToCountryDetail: (String) -> Unit = {},
    onNavigateToUniversityDetail: (String) -> Unit = {},
    onNavigateToScholarshipDetail: (String) -> Unit = {},
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(id = R.string.track_tab_checklist),
        stringResource(id = R.string.track_tab_saved),
    )

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
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
            0 -> DocumentsRoute(modifier = Modifier.padding())

            1 -> SavedRoute(
                onNavigateToCountryDetail = onNavigateToCountryDetail,
                onNavigateToUniversityDetail = onNavigateToUniversityDetail,
                onNavigateToScholarshipDetail = onNavigateToScholarshipDetail,
                modifier = Modifier.padding(),
            )
        }
    }
}
