package com.jetbrains.apiclient.submission

object SubmissionConfig {
    // TODO: set SUBMIT_API_URL when ready :)
    private const val ENV_SUBMIT_API_URL = "SUBMIT_API_URL"

    fun baseUrl(): String =
        System.getenv(ENV_SUBMIT_API_URL)?.trim()?.removeSuffix("/") ?: "http://localhost:8080/api"

    fun submissionEndpoint(): String = "${baseUrl()}/submit"
}
