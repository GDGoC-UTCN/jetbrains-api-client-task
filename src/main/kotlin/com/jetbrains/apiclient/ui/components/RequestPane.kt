package com.jetbrains.apiclient.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.jetbrains.apiclient.model.HttpMethod
import com.jetbrains.apiclient.resources.Strings

@Composable
fun RequestPaneHeader(
    requestName: String?,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Text(
            text = requestName ?: Strings.Request.TITLE,
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun RequestPane(
    url: String,
    onUrlChange: (String) -> Unit,
    method: HttpMethod,
    onMethodChange: (HttpMethod) -> Unit,
    headers: String,
    onHeadersChange: (String) -> Unit,
    body: String,
    onBodyChange: (String) -> Unit,
    isSending: Boolean,
    onSendClick: () -> Unit,
    requestName: String?,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(Strings.Request.HEADERS, Strings.Request.BODY)

    Card(
        modifier = modifier.fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            RequestPaneHeader(requestName = requestName)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var methodExpanded by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.width(100.dp)) {
                        Surface(
                            modifier = Modifier
                                .width(100.dp)
                                .height(56.dp)
                                .clickable { methodExpanded = true },
                            shape = RoundedCornerShape(0.dp),
                            border = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.outline
                            ),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp, vertical = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    method.display,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = methodExpanded,
                            onDismissRequest = { methodExpanded = false }
                        ) {
                            HttpMethod.entries.forEach { m ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            m.display,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    onClick = {
                                        onMethodChange(m)
                                        methodExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = url,
                        onValueChange = onUrlChange,
                        modifier = Modifier.weight(1f),
                        placeholder = { Text(Strings.Request.URL_PLACEHOLDER) },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Monospace
                        )
                    )
                    Button(
                        onClick = onSendClick,
                        enabled = !isSending,
                        modifier = Modifier.height(56.dp),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(Strings.ControlPanel.SEND)
                    }
                }

                Spacer(Modifier.height(12.dp))

                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    when (selectedTab) {
                        0 -> OutlinedTextField(
                            value = headers,
                            onValueChange = onHeadersChange,
                            modifier = Modifier.fillMaxSize(),
                            placeholder = { Text("{}") },
                            textStyle = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace
                            ),
                            minLines = 6
                        )
                        1 -> OutlinedTextField(
                            value = body,
                            onValueChange = onBodyChange,
                            modifier = Modifier.fillMaxSize(),
                            placeholder = { Text("{}") },
                            textStyle = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace
                            ),
                            minLines = 6
                        )
                    }
                }
            }
        }
    }
}
