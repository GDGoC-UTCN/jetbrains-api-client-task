package com.jetbrains.apiclient.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jetbrains.apiclient.resources.Strings

@Composable
fun StatusPanel(
    statusText: String,
    modifier: Modifier = Modifier,
    onCheckProgress: (() -> Unit)? = null,
    onSubmitSolution: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp, start = 12.dp, end = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onCheckProgress != null) {
                Button(
                    onClick = onCheckProgress,
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(Strings.Verification.CHECK_PROGRESS, style = MaterialTheme.typography.labelMedium)
                }
                Spacer(Modifier.width(12.dp))
            }
            if (onSubmitSolution != null) {
                Button(
                    onClick = onSubmitSolution,
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(Strings.Submit.BUTTON, style = MaterialTheme.typography.labelMedium)
                }
                Spacer(Modifier.width(12.dp))
            }
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = statusText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}
