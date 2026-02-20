package com.jetbrains.apiclient.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
enum class HttpMethod(val display: String) {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE")
}

@Serializable
data class ApiRequest(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var url: String = "",
    var method: HttpMethod = HttpMethod.GET,
    var headers: String = """{
  "Content-Type": "application/json",
  "Authorization": "Bearer ..."
}""".trimIndent(),
    var body: String = "{}"
)
