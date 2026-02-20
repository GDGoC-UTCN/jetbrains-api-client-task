package com.jetbrains.apiclient.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.jetbrains.apiclient.model.ApiRequest
import com.jetbrains.apiclient.resources.Strings
import com.jetbrains.apiclient.helpers.StorageHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RequestsViewModel(private val coroutineScope: CoroutineScope) {

    private val _requests = MutableStateFlow<List<ApiRequest>>(emptyList())
    val requests: StateFlow<List<ApiRequest>> = _requests.asStateFlow()

    val selectedRequest = mutableStateOf<ApiRequest?>(null)

    val showNewRequestDialog = mutableStateOf(false)
    val showRenameRequestDialog = mutableStateOf<ApiRequest?>(null)
    val showDeleteRequestDialog = mutableStateOf<ApiRequest?>(null)

    val statusMessage = mutableStateOf(Strings.Status.READY)

    init {
        coroutineScope.launch {
            _requests.value = StorageHelper.loadRequests()
            if (_requests.value.isEmpty()) {
                _requests.value = listOf(ApiRequest(name = "New request"))
                save()
            }
            ensureSelection()
        }
    }

    fun loadRequests() {
        coroutineScope.launch {
            _requests.value = StorageHelper.loadRequests()
            ensureSelection()
        }
    }

    private fun ensureSelection() {
        if (selectedRequest.value == null && _requests.value.isNotEmpty()) {
            selectedRequest.value = _requests.value.first()
        }
    }

    private fun save() {
        coroutineScope.launch {
            StorageHelper.saveRequests(_requests.value)
        }
    }

    fun addRequest(name: String) {
        if (name.isBlank()) return
        val newRequest = ApiRequest(name = name.trim())
        _requests.value = _requests.value + newRequest
        selectedRequest.value = newRequest
        save()
        showNewRequestDialog.value = false
        statusMessage.value = "Request created: ${newRequest.name}"
    }

    fun renameRequest(request: ApiRequest, newName: String) {
        if (newName.isBlank()) return
        val idx = _requests.value.indexOfFirst { it.id == request.id }
        if (idx < 0) return
        val updated = request.copy(name = newName.trim())
        _requests.value = _requests.value.toMutableList().apply { set(idx, updated) }
        if (selectedRequest.value?.id == request.id) {
            selectedRequest.value = updated
        }
        save()
        showRenameRequestDialog.value = null
        statusMessage.value = "Renamed: ${newName.trim()}"
    }

    fun deleteRequest(request: ApiRequest) {
        _requests.value = _requests.value.filter { it.id != request.id }
        if (selectedRequest.value?.id == request.id) {
            selectedRequest.value = _requests.value.firstOrNull()
        }
        save()
        showDeleteRequestDialog.value = null
        statusMessage.value = Strings.Status.READY
    }

    fun selectRequest(request: ApiRequest?) {
        selectedRequest.value = request
    }

    fun updateCurrentRequest(
        url: String? = null,
        method: com.jetbrains.apiclient.model.HttpMethod? = null,
        headers: String? = null,
        body: String? = null
    ) {
        val req = selectedRequest.value ?: return
        val idx = _requests.value.indexOfFirst { it.id == req.id }
        if (idx < 0) return
        val updated = req.copy(
            url = url ?: req.url,
            method = method ?: req.method,
            headers = headers ?: req.headers,
            body = body ?: req.body
        )
        _requests.value = _requests.value.toMutableList().apply { set(idx, updated) }
        selectedRequest.value = updated
        save()
    }

    fun showNewRequestDialog() {
        showNewRequestDialog.value = true
    }

    fun showRenameRequestDialog(request: ApiRequest) {
        showRenameRequestDialog.value = request
    }

    fun showDeleteRequestDialog(request: ApiRequest) {
        showDeleteRequestDialog.value = request
    }

    fun dismissNewRequestDialog() {
        showNewRequestDialog.value = false
    }

    fun dismissRenameRequestDialog() {
        showRenameRequestDialog.value = null
    }

    fun dismissDeleteRequestDialog() {
        showDeleteRequestDialog.value = null
    }
}
