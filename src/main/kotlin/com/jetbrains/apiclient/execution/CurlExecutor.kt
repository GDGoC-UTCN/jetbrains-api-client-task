package com.jetbrains.apiclient.execution

import com.jetbrains.apiclient.model.ApiRequest
import com.jetbrains.apiclient.model.HttpMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.jsonObject
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

object CurlExecutor {

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun execute(
        request: ApiRequest,
        onOutput: (String) -> Unit
    ): Int = withContext(Dispatchers.IO) {
        val url = request.url.trim()
        if (url.isBlank()) {
            onOutput("Error: URL is empty")
            return@withContext -1
        }

        val headersTrimmed = request.headers.trim()
        if (headersTrimmed.isNotEmpty() && parseHeaders(request.headers) == null) {
            onOutput("Error: Headers must be valid JSON. Example: {\"Content-Type\": \"application/json\"}")
            return@withContext -1
        }

        val cmd = buildCurlCommand(request)
        val processBuilder = ProcessBuilder(cmd)
        processBuilder.redirectErrorStream(false)
        processBuilder.directory(File(System.getProperty("user.dir")))

        try {
            val process = processBuilder.start()

            if (request.body.isNotBlank() && sendsBody(request.method)) {
                process.outputStream.use { out ->
                    out.write(request.body.trim().toByteArray(StandardCharsets.UTF_8))
                    out.flush()
                }
            }

            val stdout = process.inputStream
            val stderr = process.errorStream
            val stdoutText = InputStreamReader(stdout, StandardCharsets.UTF_8).readText()
            val stderrText = InputStreamReader(stderr, StandardCharsets.UTF_8).readText()

            if (stdoutText.isNotBlank()) {
                onOutput(stdoutText)
            }
            if (stderrText.isNotBlank()) {
                onOutput(stderrText)
            }

            process.waitFor()
        } catch (e: Exception) {
            onOutput("Error: ${e.message ?: "Failed to run curl"}")
            -1
        }
    }

    private fun sendsBody(method: HttpMethod): Boolean =
        method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH

    private fun buildCurlCommand(request: ApiRequest): List<String> {
        val cmd = mutableListOf("curl", "-s", "-S", "-X", request.method.name, request.url.trim())
        val parsed = parseHeaders(request.headers)
        if (parsed != null) {
            parsed.forEach { (key, value) ->
                cmd.add("-H")
                cmd.add("$key: $value")
            }
        }
        if (request.body.isNotBlank() && sendsBody(request.method)) {
            cmd.add("-d")
            cmd.add("@-")
        }
        return cmd
    }

    private fun parseHeaders(headers: String): List<Pair<String, String>>? {
        val trimmed = headers.trim()
        if (trimmed.isEmpty()) return emptyList()
        if (!trimmed.startsWith("{")) return null
        return try {
            val obj = json.parseToJsonElement(trimmed).jsonObject
            obj.map { (key, value) ->
                val str = try {
                    value.jsonPrimitive.content
                } catch (_: Exception) {
                    value.toString()
                }
                key to str
            }
        } catch (_: Exception) {
            null
        }
    }
}
