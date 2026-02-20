package com.jetbrains.apiclient.ui.components

import androidx.compose.runtime.Composable
import com.jetbrains.apiclient.ui.dialogs.*
import com.jetbrains.apiclient.viewmodel.RequestsViewModel

@Composable
fun DialogsContainer(viewModel: RequestsViewModel) {
    if (viewModel.showNewRequestDialog.value) {
        NewRequestDialog(
            onConfirm = { viewModel.addRequest(it) },
            onDismiss = { viewModel.dismissNewRequestDialog() }
        )
    }
    viewModel.showRenameRequestDialog.value?.let { request ->
        RenameRequestDialog(
            request = request,
            onConfirm = { viewModel.renameRequest(request, it) },
            onDismiss = { viewModel.dismissRenameRequestDialog() }
        )
    }
    viewModel.showDeleteRequestDialog.value?.let { request ->
        DeleteRequestDialog(
            request = request,
            onConfirm = { viewModel.deleteRequest(request) },
            onDismiss = { viewModel.dismissDeleteRequestDialog() }
        )
    }
}
