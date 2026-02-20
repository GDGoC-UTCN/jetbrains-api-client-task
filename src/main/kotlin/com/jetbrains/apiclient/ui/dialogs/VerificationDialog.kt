package com.jetbrains.apiclient.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jetbrains.apiclient.resources.Strings
import com.jetbrains.apiclient.verification.VerificationResult

@Composable
fun VerificationDialog(
    isRunning: Boolean,
    results: List<VerificationResult>?,
    notAvailable: Boolean,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(Strings.Verification.TITLE) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (isRunning) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Text(Strings.Verification.RUNNING)
                    }
                } else if (notAvailable) {
                    Text(Strings.Verification.NOT_AVAILABLE)
                } else if (results != null) {
                    Spacer(Modifier.height(8.dp))
                    results.forEach { r ->
                        Text(
                            r.displayText,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(Strings.Common.OK)
            }
        }
    )
}
