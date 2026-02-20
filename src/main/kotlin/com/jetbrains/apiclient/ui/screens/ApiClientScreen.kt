package com.jetbrains.apiclient.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jetbrains.apiclient.ui.components.*
import com.jetbrains.apiclient.resources.Strings
import com.jetbrains.apiclient.submission.SubmissionService
import com.jetbrains.apiclient.ui.dialogs.SubmitDialog
import com.jetbrains.apiclient.ui.dialogs.VerificationDialog
import com.jetbrains.apiclient.verification.VerificationResult
import com.jetbrains.apiclient.verification.VerificationRunner
import com.jetbrains.apiclient.viewmodel.ExecutionViewModel
import com.jetbrains.apiclient.viewmodel.RequestsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.file.Paths

@Composable
fun ApiClientScreen(
    requestsViewModel: RequestsViewModel,
    executionViewModel: ExecutionViewModel,
    modifier: Modifier = Modifier
) {
    val requests by requestsViewModel.requests.collectAsState()
    val selectedRequest by requestsViewModel.selectedRequest
    val statusMessage by requestsViewModel.statusMessage
    val scope = rememberCoroutineScope()

    var showVerificationDialog by remember { mutableStateOf(false) }
    var verificationRunning by remember { mutableStateOf(false) }
    var verificationResults by remember { mutableStateOf<List<VerificationResult>?>(null) }
    var verificationNotAvailable by remember { mutableStateOf(false) }

    var showSubmitDialog by remember { mutableStateOf(false) }
    var submitInProgress by remember { mutableStateOf(false) }
    var submitResultMessage by remember { mutableStateOf<String?>(null) }
    var submitResultError by remember { mutableStateOf(false) }

    fun onCheckProgress() {
        showVerificationDialog = true
        verificationRunning = true
        verificationResults = null
        verificationNotAvailable = false
        scope.launch {
            val results = withContext(Dispatchers.Default) { VerificationRunner.runDefault() }
            verificationRunning = false
            verificationResults = results
            verificationNotAvailable = (results == null)
        }
    }

    fun onSubmitSolution() {
        showSubmitDialog = true
        submitResultMessage = null
        submitResultError = false
        scope.launch(Dispatchers.Default) { SubmissionService.triggerNetworkPreflight() }
    }

    fun doSubmit(participantName: String) {
        submitInProgress = true
        submitResultMessage = null
        submitResultError = false
        scope.launch {
            val outcome = withContext(Dispatchers.Default) {
                val root = Paths.get(System.getProperty("user.dir"))
                SubmissionService.canCreateSubmissionZip(root)
                    .fold(
                        onSuccess = {
                            val zipBytes = SubmissionService.createSubmissionZip(root)
                            val results = SubmissionService.runVerification() ?: emptyList()
                            SubmissionService.submit(participantName, zipBytes, results)
                        },
                        onFailure = { Result.failure(it) }
                    )
            }
            submitInProgress = false
            submitResultMessage = outcome.fold(
                onSuccess = { Strings.Submit.SUCCESS },
                onFailure = { Strings.Submit.ERROR_PREFIX + (it.message ?: it.toString()) }
            )
            submitResultError = outcome.isFailure
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatusPanel(
            statusText = statusMessage,
            onCheckProgress = ::onCheckProgress,
            onSubmitSolution = ::onSubmitSolution
        )

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

        if (showVerificationDialog) {
            VerificationDialog(
                isRunning = verificationRunning,
                results = verificationResults,
                notAvailable = verificationNotAvailable,
                onDismiss = { showVerificationDialog = false }
            )
        }

        if (showSubmitDialog) {
            SubmitDialog(
                isSubmitting = submitInProgress,
                resultMessage = submitResultMessage,
                isError = submitResultError,
                onDismiss = {
                    showSubmitDialog = false
                    submitResultMessage = null
                },
                onRequestSubmit = ::doSubmit
            )
        }
    }
}
