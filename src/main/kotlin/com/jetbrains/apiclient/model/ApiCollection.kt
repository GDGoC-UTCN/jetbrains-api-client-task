package com.jetbrains.apiclient.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ApiCollection(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    val requests: MutableList<ApiRequest> = mutableListOf()
)
