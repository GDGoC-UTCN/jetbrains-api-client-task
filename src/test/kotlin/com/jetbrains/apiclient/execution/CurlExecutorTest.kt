package com.jetbrains.apiclient.execution

import com.jetbrains.apiclient.model.ApiRequest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CurlExecutorTest {

    @Test
    fun test1() = runTest {
        val request = ApiRequest(name = "Test", url = "  ")
        val outputLines = mutableListOf<String>()
        val exitCode = CurlExecutor.execute(request) { outputLines.add(it) }
        assertEquals(-1, exitCode)
        assertTrue(outputLines.any { it.contains("URL is empty") })
    }

    @Test
    fun test2() = runTest {
        val request = ApiRequest(name = "Test", url = "")
        val outputLines = mutableListOf<String>()
        val exitCode = CurlExecutor.execute(request) { outputLines.add(it) }
        assertEquals(-1, exitCode)
    }

    @Test
    fun test3() = runTest {
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
    fun test4() = runTest {
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
    fun test5() = runTest {
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
    fun test6() = runTest {
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
