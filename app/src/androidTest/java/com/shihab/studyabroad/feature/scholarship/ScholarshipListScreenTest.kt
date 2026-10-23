package com.shihab.studyabroad.feature.scholarship

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.shihab.studyabroad.data.repository.SampleData
import com.shihab.studyabroad.domain.model.ScholarshipStatus
import org.junit.Rule
import org.junit.Test

class ScholarshipListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun scholarshipListScreen_displaysItemsAndStatusLabels() {
        val sampleScholarship = SampleData.sampleScholarships.first()
        val contentData = ScholarshipListData(
            items = listOf(
                ScholarshipItemState(
                    scholarship = sampleScholarship,
                    status = ScholarshipStatus.OPEN,
                ),
            ),
        )

        composeTestRule.setContent {
            ScholarshipListScreen(
                uiState = ScholarshipListUiState.Content(contentData),
            )
        }

        // Verify scholarship name is displayed
        composeTestRule.onNodeWithText(sampleScholarship.name).assertIsDisplayed()

        // Verify status label "Open" is displayed
        composeTestRule.onNodeWithText("Open").assertIsDisplayed()
    }
}
