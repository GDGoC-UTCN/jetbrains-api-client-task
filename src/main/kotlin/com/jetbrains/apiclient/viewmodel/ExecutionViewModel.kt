package com.jetbrains.apiclient.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.jetbrains.apiclient.execution.CurlExecutor
import com.jetbrains.apiclient.model.ApiRequest
import com.jetbrains.apiclient.resources.Strings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ExecutionViewModel(private val coroutineScope: CoroutineScope) {

    val isRunning = mutableStateOf(false)
    val outputContent = mutableStateOf("")

    private var executionJob: Job? = null

    fun sendRequest(request: ApiRequest?, onStatusUpdate: (String) -> Unit) {
        if (request == null) {
            outputContent.value = "Select or create a request first."
            return
        }
        if (isRunning.value) return

        isRunning.value = true
        onStatusUpdate(Strings.Status.SENDING)
        outputContent.value = ""

        executionJob =
                coroutineScope.launch {
                    try {
                        val exitCode =
                                CurlExecutor.execute(request) { line ->
                                    outputContent.value += line + "\n"
                                }
                        onStatusUpdate(
                                if (exitCode == 0) Strings.Status.SUCCESS
                                else Strings.Status.ERROR_FORMAT.format("Exit code: $exitCode")
                        )
                    } catch (e: Exception) {
                        outputContent.value +=
                                (Strings.Status.ERROR_FORMAT.format(
                                        e.message ?: Strings.Status.UNKNOWN_ERROR
                                )) + "\n"
                        onStatusUpdate(Strings.Status.ERROR)
                    } finally {
                        isRunning.value = false
                    }
                }
    }

    fun clearOutput() {
        outputContent.value = ""
    }
}
