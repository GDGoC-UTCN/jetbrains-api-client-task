package com.jetbrains.apiclient.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jetbrains.apiclient.resources.Strings

@Composable
fun SubmitDialog(
    isSubmitting: Boolean,
    resultMessage: String?,
    isError: Boolean,
    onDismiss: () -> Unit,
    onRequestSubmit: (participantName: String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(Strings.Submit.DIALOG_TITLE) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                when {
                    resultMessage != null -> {
                        Text(
                            text = resultMessage,
                            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    isSubmitting -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            Text(Strings.Submit.SUBMITTING)
                        }
                    }
                    else -> {
                        Text(Strings.Submit.CONFIRM_MESSAGE)
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text(Strings.Submit.NAME_LABEL) },
                            placeholder = { Text(Strings.Submit.NAME_PLACEHOLDER) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {
            when {
                resultMessage != null -> TextButton(onClick = onDismiss) { Text(Strings.Common.OK) }
                isSubmitting -> { }
                else -> Button(
                    onClick = { if (name.isNotBlank()) onRequestSubmit(name.trim()) },
                    enabled = name.isNotBlank()
                ) {
                    Text(Strings.Submit.SUBMIT)
                }
            }
        },
        dismissButton = {
            if (resultMessage == null && !isSubmitting) {
                TextButton(onClick = onDismiss) {
                    Text(Strings.Submit.CANCEL)
                }
            }
        }
    )
}
