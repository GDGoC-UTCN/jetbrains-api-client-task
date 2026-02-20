package com.jetbrains.apiclient.verification

import kotlinx.serialization.Serializable

@Serializable
data class VerificationResult(
    val moduleName: String,
    val passed: Int,
    val total: Int
) {
    val displayText: String get() = "$moduleName: $passed/$total"
}
