package com.jetbrains.apiclient.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jetbrains.apiclient.resources.Strings

@Composable
fun ResponsePaneHeader(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = Strings.Response.TITLE,
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun ResponsePane(
    outputContent: String,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // That autoscroll to bottom thingie cool kids have
    LaunchedEffect(outputContent) {
        kotlinx.coroutines.delay(50)
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Card(
        modifier = modifier.fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ResponsePaneHeader()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(scrollState)
            ) {
                SelectionContainer {
                    Text(
                        text = outputContent.ifBlank { Strings.Response.EMPTY },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}
