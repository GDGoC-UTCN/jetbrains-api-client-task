package com.jetbrains.apiclient.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jetbrains.apiclient.ui.components.*
import com.jetbrains.apiclient.viewmodel.ExecutionViewModel
import com.jetbrains.apiclient.viewmodel.RequestsViewModel

@Composable
fun ApiClientScreen(
    requestsViewModel: RequestsViewModel,
    executionViewModel: ExecutionViewModel,
    modifier: Modifier = Modifier
) {
    val requests by requestsViewModel.requests.collectAsState()
    val selectedRequest by requestsViewModel.selectedRequest
    val statusMessage by requestsViewModel.statusMessage

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatusPanel(statusText = statusMessage)

        Row(
            modifier = Modifier.fillMaxSize().weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RequestsPanel(
                requests = requests,
                selectedRequest = selectedRequest,
                onSelectRequest = { requestsViewModel.selectRequest(it) },
                onAddRequest = { requestsViewModel.showNewRequestDialog() },
                onRenameRequest = { requestsViewModel.showRenameRequestDialog(it) },
                onDeleteRequest = { requestsViewModel.showDeleteRequestDialog(it) },
                modifier = Modifier.width(240.dp)
            )

            Column(
                modifier = Modifier.weight(1f).fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RequestPane(
                    url = selectedRequest?.url ?: "",
                    onUrlChange = { requestsViewModel.updateCurrentRequest(url = it) },
                    method = selectedRequest?.method ?: com.jetbrains.apiclient.model.HttpMethod.GET,
                    onMethodChange = { requestsViewModel.updateCurrentRequest(method = it) },
                    headers = selectedRequest?.headers ?: "",
                    onHeadersChange = { requestsViewModel.updateCurrentRequest(headers = it) },
                    body = selectedRequest?.body ?: "",
                    onBodyChange = { requestsViewModel.updateCurrentRequest(body = it) },
                    isSending = executionViewModel.isRunning.value,
                    onSendClick = {
                        requestsViewModel.updateCurrentRequest(
                            url = selectedRequest?.url,
                            headers = selectedRequest?.headers,
                            body = selectedRequest?.body
                        )
                        executionViewModel.sendRequest(selectedRequest) { requestsViewModel.statusMessage.value = it }
                    },
                    requestName = selectedRequest?.name,
                    modifier = Modifier.weight(1f)
                )

                ResponsePane(
                    outputContent = executionViewModel.outputContent.value,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        DialogsContainer(viewModel = requestsViewModel)
    }
}
