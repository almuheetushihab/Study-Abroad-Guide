package com.shihab.studyabroad.feature.debug

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.core.designsystem.component.AppTextField
import com.shihab.studyabroad.core.designsystem.component.AppTopBar
import com.shihab.studyabroad.core.designsystem.component.FilterChipRow
import com.shihab.studyabroad.core.designsystem.component.InfoCard
import com.shihab.studyabroad.core.designsystem.component.LabeledValue
import com.shihab.studyabroad.core.designsystem.component.OfflineBanner
import com.shihab.studyabroad.core.designsystem.component.PrimaryButton
import com.shihab.studyabroad.core.designsystem.component.ProgressBarWithLabel
import com.shihab.studyabroad.core.designsystem.component.ScamAlertCard
import com.shihab.studyabroad.core.designsystem.component.SecondaryButton
import com.shihab.studyabroad.core.designsystem.component.SectionHeader
import com.shihab.studyabroad.core.designsystem.component.StaleBadge
import com.shihab.studyabroad.core.designsystem.component.StatusChip
import com.shihab.studyabroad.core.designsystem.component.VerifiedFooter
import com.shihab.studyabroad.core.designsystem.component.WarningBanner
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.domain.model.ScholarshipStatus

/**
 * Debug-only Theme Preview screen showing all design system components and tokens.
 */
@Composable
fun ThemePreviewScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    var textValue by remember { mutableStateOf("Sample input") }
    var selectedFilter by remember { mutableStateOf("All") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = "Theme & Components Preview",
                onBack = onBack,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            Text(
                text = "Status Chips",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            StatusChip(status = ScholarshipStatus.OPEN)
            Spacer(modifier = Modifier.height(4.dp))
            StatusChip(status = ScholarshipStatus.OPENING_SOON)
            Spacer(modifier = Modifier.height(4.dp))
            StatusChip(status = ScholarshipStatus.CLOSED)

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Badges & Banners",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            StaleBadge()
            Spacer(modifier = Modifier.height(8.dp))
            WarningBanner(message = "Sample warning advisory message.")
            Spacer(modifier = Modifier.height(8.dp))
            ScamAlertCard()

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Info Card & Section Header",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(title = "Featured Scholarships", actionText = "See All", onActionClick = {})
            InfoCard(title = "Stipendium Hungaricum") {
                LabeledValue(label = "Deadline", value = "15 Jan 2027")
                LabeledValue(label = "Tuition Fee", value = "Free (Covered)")
                LabeledValue(label = "Living Cost", value = "EUR 375 - 650 / mo")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Verified Footer",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            VerifiedFooter(
                sourceUrl = "https://stipendiumhungaricum.hu",
                verifiedOn = "15 Oct 2026",
                onOpenSource = {},
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Filters & Progress",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            FilterChipRow(
                options = listOf("All", "Fresher", "Graduate", "Professional"),
                selectedOption = selectedFilter,
                onSelectOption = { selectedFilter = it },
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProgressBarWithLabel(progress = 0.65f, label = "Application Readiness")

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Buttons & Text Inputs",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(text = "Primary Action", onClick = {})
            Spacer(modifier = Modifier.height(8.dp))
            SecondaryButton(text = "Secondary Action", onClick = {})
            Spacer(modifier = Modifier.height(8.dp))
            AppTextField(
                value = textValue,
                onValueChange = { textValue = it },
                label = "Full Name",
                supportingText = "As per passport",
            )

            Spacer(modifier = Modifier.height(16.dp))
            OfflineBanner(lastUpdated = "15 Oct 2026")
        }
    }
}

@ThemePreviews
@Composable
fun ThemePreviewScreenPreview() {
    StudyAbroadTheme {
        ThemePreviewScreen(onBack = {})
    }
}
