package com.jetbrains.apiclient.execution

import com.jetbrains.apiclient.model.ApiRequest
import com.jetbrains.apiclient.model.HttpMethod
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CurlExecutorTest {

    @Test
    fun `execute with blank URL returns -1 and outputs error`() = runTest {
        val request = ApiRequest(name = "Test", url = "  ")
        val outputLines = mutableListOf<String>()
        val exitCode = CurlExecutor.execute(request) { outputLines.add(it) }
        assertEquals(-1, exitCode)
        assertTrue(outputLines.any { it.contains("URL is empty") })
    }

    @Test
    fun `execute with empty URL string returns -1`() = runTest {
        val request = ApiRequest(name = "Test", url = "")
        val outputLines = mutableListOf<String>()
        val exitCode = CurlExecutor.execute(request) { outputLines.add(it) }
        assertEquals(-1, exitCode)
    }

    @Test
    fun `execute with invalid headers JSON returns -1 and outputs error`() = runTest {
        val request = ApiRequest(
            name = "Test",
            url = "https://example.com",
            headers = "not valid json"
        )
        val outputLines = mutableListOf<String>()
        val exitCode = CurlExecutor.execute(request) { outputLines.add(it) }
        assertEquals(-1, exitCode)
        assertTrue(outputLines.any { it.contains("Headers must be valid JSON") })
    }

    @Test
    fun `execute with headers that are not a JSON object returns -1`() = runTest {
        val request = ApiRequest(
            name = "Test",
            url = "https://example.com",
            headers = "\"just a string\""
        )
        val outputLines = mutableListOf<String>()
        val exitCode = CurlExecutor.execute(request) { outputLines.add(it) }
        assertEquals(-1, exitCode)
    }

    @Test
    fun `execute with empty headers string does not fail validation`() = runTest {
        val request = ApiRequest(
            name = "Test",
            url = "https://example.com",
            headers = ""
        )
        val outputLines = mutableListOf<String>()
        val exitCode = CurlExecutor.execute(request) { outputLines.add(it) }
        assertTrue(exitCode == 0 || exitCode == -1)
        assertFalse(outputLines.any { it.contains("Headers must be valid JSON") })
    }

    @Test
    fun `execute with valid JSON object headers passes validation`() = runTest {
        val request = ApiRequest(
            name = "Test",
            url = "https://example.com",
            headers = """{"Content-Type": "application/json"}"""
        )
        val outputLines = mutableListOf<String>()
        val exitCode = CurlExecutor.execute(request) { outputLines.add(it) }
        assertFalse(outputLines.any { it.contains("Headers must be valid JSON") })
        assertTrue(exitCode == 0 || exitCode == -1)
    }
}
