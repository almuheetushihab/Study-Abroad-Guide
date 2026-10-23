package com.shihab.studyabroad.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shihab.studyabroad.R

@Composable
fun ReportWrongInfoDialog(onDismiss: () -> Unit, onSubmit: (String) -> Unit, modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.report_info_dialog_title),
                style = MaterialTheme.typography.titleMedium,
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        if (it.isNotBlank()) isError = false
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.report_info_hint))
                    },
                    isError = isError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                    maxLines = 5,
                )
                if (isError) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = R.string.report_info_error_empty),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (text.isBlank()) {
                        isError = true
                    } else {
                        onSubmit(text.trim())
                    }
                },
                modifier = Modifier.heightIn(min = 48.dp),
            ) {
                Text(text = stringResource(id = R.string.submit))
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.heightIn(min = 48.dp),
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        modifier = modifier,
    )
}
