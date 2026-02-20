package com.jetbrains.apiclient.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ApiRequestDto(
    val id: String,
    val name: String,
    val url: String,
    val method: String,
    val headers: String,
    val body: String
)

internal fun ApiRequestDto.toApiRequest(): ApiRequest = ApiRequest(
    id = id,
    name = name,
    url = url,
    method = HttpMethod.entries.find { it.name == method } ?: HttpMethod.GET,
    headers = headers,
    body = body
)

internal fun ApiRequest.toDto(): ApiRequestDto = ApiRequestDto(
    id = id,
    name = name,
    url = url,
    method = method.name,
    headers = headers,
    body = body
)
