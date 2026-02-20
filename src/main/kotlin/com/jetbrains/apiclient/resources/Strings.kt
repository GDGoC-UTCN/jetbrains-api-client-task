package com.jetbrains.apiclient.resources

object Strings {
    object App {
        const val TITLE = "API Client"
    }

    object ControlPanel {
        const val SEND = "Send"
        const val READY = "Ready"
        const val SENDING = "Sending..."
    }

    object Requests {
        const val TITLE = "Requests"
        const val ADD_REQUEST = "+ Request"
        const val RENAME = "Rename"
        const val DELETE = "Delete"
    }

    object Request {
        const val TITLE = "Request"
        const val URL_PLACEHOLDER = "https://api.example.com/..."
        const val HEADERS = "Headers"
        const val BODY = "Body"
    }

    object Response {
        const val TITLE = "Response"
        const val EMPTY = "Send a request to see the response."
    }

    object Dialogs {
        object Collection {
            const val NEW_TITLE = "New Collection"
            const val RENAME_TITLE = "Rename Collection"
            const val NAME_LABEL = "Collection name"
            const val NAME_PLACEHOLDER = "My Collection"
            const val CONFIRM = "Create"
            const val RENAME_CONFIRM = "Rename"
            const val CANCEL = "Cancel"
        }

        object Request {
            const val NEW_TITLE = "New Request"
            const val RENAME_TITLE = "Rename Request"
            const val NAME_LABEL = "Request name"
            const val NAME_PLACEHOLDER = "Get Users"
            const val CONFIRM = "Create"
            const val RENAME_CONFIRM = "Rename"
            const val CANCEL = "Cancel"
        }

        object Delete {
            const val COLLECTION_TITLE = "Delete Collection"
            const val REQUEST_TITLE = "Delete Request"
            const val COLLECTION_MESSAGE = "Delete collection '%s' and all its requests?"
            const val REQUEST_MESSAGE = "Delete request '%s'?"
            const val CONFIRM = "Delete"
            const val CANCEL = "Cancel"
        }
    }

    object Status {
        const val READY = "Ready"
        const val SENDING = "Sending..."
        const val SUCCESS = "Success"
        const val ERROR = "Error"
        const val ERROR_FORMAT = "Error: %s"
        const val UNKNOWN_ERROR = "Unknown error"
    }
}
