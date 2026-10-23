package com.shihab.studyabroad.feature.profilesetup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.R
import com.shihab.studyabroad.domain.model.Country
import com.shihab.studyabroad.domain.model.FundingGoal
import com.shihab.studyabroad.domain.usecase.ProfileValidationResult
import com.shihab.studyabroad.feature.profilesetup.ProfileFormState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Step4BudgetCountries(
    formState: ProfileFormState,
    validationResult: ProfileValidationResult,
    availableCountries: List<Country>,
    onBudgetRangeChanged: (String) -> Unit,
    onGoalChanged: (FundingGoal) -> Unit,
    onCountryToggled: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val budgetOptions = listOf(
        "< 5 Lakh BDT",
        "5-10 Lakh BDT",
        "10-20 Lakh BDT",
        "> 20 Lakh BDT",
        "Fully Funded Only",
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.label_budget_range),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(modifier = Modifier.fillMaxWidth()) {
            budgetOptions.forEach { option ->
                val isSelected = formState.budgetRange == option
                FilterChip(
                    selected = isSelected,
                    onClick = { onBudgetRangeChanged(option) },
                    label = { Text(text = option) },
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                )
            }
        }

        if (!validationResult.isBudgetValid && validationResult.budgetError != null) {
            Text(
                text = validationResult.budgetError.asString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.label_funding_goal),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            RadioButton(
                selected = formState.goal == FundingGoal.SCHOLARSHIP,
                onClick = { onGoalChanged(FundingGoal.SCHOLARSHIP) },
            )
            Text(
                text = stringResource(id = R.string.goal_scholarship),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 4.dp),
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            RadioButton(
                selected = formState.goal == FundingGoal.SELF_FUNDED,
                onClick = { onGoalChanged(FundingGoal.SELF_FUNDED) },
            )
            Text(
                text = stringResource(id = R.string.goal_self_funded),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 4.dp),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.label_target_countries),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(modifier = Modifier.fillMaxWidth()) {
            availableCountries.forEach { country ->
                val isSelected = formState.selectedCountryIds.contains(country.id)
                FilterChip(
                    selected = isSelected,
                    onClick = { onCountryToggled(country.id) },
                    label = { Text(text = country.name) },
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                )
            }
        }

        if (!validationResult.isCountriesValid && validationResult.countriesError != null) {
            Text(
                text = validationResult.countriesError.asString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}
