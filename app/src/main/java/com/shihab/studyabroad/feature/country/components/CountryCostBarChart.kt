package com.shihab.studyabroad.feature.country.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.R
import com.shihab.studyabroad.core.designsystem.theme.StudyAbroadTheme
import com.shihab.studyabroad.core.ui.ThemePreviews
import com.shihab.studyabroad.data.repository.SampleData
import com.shihab.studyabroad.domain.model.Country

/**
 * Custom Canvas Bar Chart for comparing annual living costs without third-party chart libraries.
 */
@Composable
fun CountryCostBarChart(selectedCountries: List<Country>, modifier: Modifier = Modifier) {
    if (selectedCountries.isEmpty()) return

    val textMeasurer = rememberTextMeasurer()
    val primaryColor = MaterialTheme.colorScheme.primary
    val labelStyle = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurface)

    // Calculate approximate relative cost values for visual bar drawing
    val countryCosts = selectedCountries.map { country ->
        country.name to parseCostValue(country.livingRangeText)
    }
    val maxCost = (countryCosts.maxOfOrNull { it.second } ?: 1000).coerceAtLeast(1)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.country_chart_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(16.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
            ) {
                val width = size.width
                val height = size.height
                val barCount = countryCosts.size
                val spaceBetween = 24.dp.toPx()
                val totalSpace = spaceBetween * (barCount + 1)
                val barWidth = ((width - totalSpace) / barCount).coerceAtLeast(20.dp.toPx())

                // Draw baseline
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, height - 30.dp.toPx()),
                    end = Offset(width, height - 30.dp.toPx()),
                    strokeWidth = 2f,
                )

                countryCosts.forEachIndexed { index, (name, cost) ->
                    val x = spaceBetween + index * (barWidth + spaceBetween)
                    val barHeight = ((cost.toFloat() / maxCost) * (height - 60.dp.toPx())).coerceAtLeast(10f)
                    val y = height - 30.dp.toPx() - barHeight

                    // Draw Bar
                    drawRect(
                        color = primaryColor,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight),
                    )

                    // Draw Country Name Label below bar
                    val nameLayout = textMeasurer.measure(text = name, style = labelStyle)
                    drawText(
                        textLayoutResult = nameLayout,
                        topLeft = Offset(x + (barWidth - nameLayout.size.width) / 2, height - 24.dp.toPx()),
                    )

                    // Draw Cost Label above bar
                    val costLayout = textMeasurer.measure(text = "$cost/mo", style = labelStyle)
                    drawText(
                        textLayoutResult = costLayout,
                        topLeft = Offset(x + (barWidth - costLayout.size.width) / 2, (y - 20.dp.toPx()).coerceAtLeast(0f)),
                    )
                }
            }
        }
    }
}

private fun parseCostValue(text: String): Int {
    val digits = text.filter { it.isDigit() }
    return if (digits.length >= 3) {
        digits.take(4).toIntOrNull() ?: 500
    } else {
        500
    }
}

@ThemePreviews
@Composable
fun CountryCostBarChartPreview() {
    StudyAbroadTheme {
        CountryCostBarChart(selectedCountries = SampleData.sampleCountries.take(3))
    }
}
