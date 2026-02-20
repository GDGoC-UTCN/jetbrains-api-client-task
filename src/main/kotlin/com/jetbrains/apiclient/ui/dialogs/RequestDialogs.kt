package com.jetbrains.apiclient.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.jetbrains.apiclient.model.ApiRequest
import com.jetbrains.apiclient.resources.Strings

@Composable
fun NewRequestDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        modifier = Modifier.testTag("new_request_dialog"),
        onDismissRequest = onDismiss,
        title = { Text(Strings.Dialogs.Request.NEW_TITLE) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Enter request name:")
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(Strings.Dialogs.Request.NAME_LABEL) },
                    placeholder = { Text(Strings.Dialogs.Request.NAME_PLACEHOLDER) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (name.isNotBlank()) onConfirm(name.trim()) },
                enabled = name.isNotBlank()
            ) { Text(Strings.Dialogs.Request.CONFIRM) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(Strings.Dialogs.Request.CANCEL) }
        }
    )
}

@Composable
fun RenameRequestDialog(
    request: ApiRequest,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember(request) { mutableStateOf(request.name) }
    AlertDialog(
        modifier = Modifier.testTag("rename_request_dialog"),
        onDismissRequest = onDismiss,
        title = { Text(Strings.Dialogs.Request.RENAME_TITLE) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(Strings.Dialogs.Request.NAME_LABEL) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (name.isNotBlank()) onConfirm(name.trim()) },
                enabled = name.isNotBlank()
            ) { Text(Strings.Dialogs.Request.RENAME_CONFIRM) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(Strings.Dialogs.Request.CANCEL) }
        }
    )
}

@Composable
fun DeleteRequestDialog(
    request: ApiRequest,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.testTag("delete_request_dialog"),
        onDismissRequest = onDismiss,
        title = { Text(Strings.Dialogs.Delete.REQUEST_TITLE) },
        text = { Text(Strings.Dialogs.Delete.REQUEST_MESSAGE.format(request.name)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(Strings.Dialogs.Delete.CONFIRM, color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(Strings.Dialogs.Delete.CANCEL) }
        }
    )
}
