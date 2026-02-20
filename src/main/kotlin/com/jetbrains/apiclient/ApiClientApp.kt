package com.jetbrains.apiclient

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.jetbrains.apiclient.ui.screens.ApiClientScreen
import com.jetbrains.apiclient.viewmodel.ExecutionViewModel
import com.jetbrains.apiclient.viewmodel.RequestsViewModel

@Composable
fun ApiClientApp(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val requestsViewModel = remember { RequestsViewModel(coroutineScope) }
    val executionViewModel = remember { ExecutionViewModel(coroutineScope) }

    ApiClientScreen(
        requestsViewModel = requestsViewModel,
        executionViewModel = executionViewModel,
        modifier = modifier.fillMaxSize()
    )
}
