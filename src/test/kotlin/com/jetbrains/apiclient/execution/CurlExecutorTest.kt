package com.jetbrains.apiclient.execution

import com.jetbrains.apiclient.model.ApiRequest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CurlExecutorTest {

    val key1 = 0x5A;
    val data1 = intArrayOf(15, 8, 22, 122, 51, 41, 122, 63, 55, 42, 46, 35)
    val text1 = data1
        .map { it xor key1 }
        .map { it.toChar() }
        .joinToString("")

    val key2 = 0x8B
    val data2 = intArrayOf(195, 238, 234, 239, 238, 249, 248, 171, 230, 254, 248, 255, 171, 233, 238, 171, 253, 234, 231, 226, 239, 171, 193, 216, 196, 197)

    val text2 = data2
        .map { it xor key2 }
        .map { it.toChar() }
        .joinToString("")

    val key3 = 0x7B
    val data3 = intArrayOf(21, 20, 15, 91, 13, 26, 23, 18, 31, 91, 17, 8, 20, 21)

    val text3 = data3
        .map { it xor key3 }
        .map { it.toChar() }
        .joinToString("")


    val key4 = 0x2D
    val data4 = intArrayOf(15, 71, 88, 94, 89, 13, 76, 13, 94, 89, 95, 68, 67, 74, 15)

    val text4 = data4
        .map { it xor key4 }
        .map { it.toChar() }
        .joinToString("")

    val key5 = 0x6E
    val data5 = intArrayOf(38, 11, 15, 10, 11, 28, 29, 78, 3, 27, 29, 26, 78, 12, 11, 78, 24, 15, 2, 7, 10, 78, 36, 61, 33, 32)

    val text5 = data5
        .map { it xor key5 }
        .map { it.toChar() }
        .joinToString("")

    val key6 = 0xF1
    val data6 = intArrayOf(138, 211, 178, 158, 159, 133, 148, 159, 133, 220, 165, 136, 129, 148, 211, 203, 209, 211, 144, 129, 129, 157, 152, 146, 144, 133, 152, 158, 159, 222, 155, 130, 158, 159, 211, 140)

    val text6 = data6
        .map { it xor key6 }
        .map { it.toChar() }
        .joinToString("")

    val key7 = 0x67
    val data7 = intArrayOf(47, 2, 6, 3, 2, 21, 20, 71, 10, 18, 20, 19, 71, 5, 2, 71, 17, 6, 11, 14, 3, 71, 45, 52, 40, 41)

    val text7 = data7
        .map { it xor key7 }
        .map { it.toChar() }
        .joinToString("")

    @Test
    fun test1() = runTest {
        val request = ApiRequest(name = "Test", url = "  ")
        val outputLines = mutableListOf<String>()
        val exitCode = CurlExecutor.execute(request) { outputLines.add(it) }
        assertEquals(-1, exitCode)
        assertTrue(outputLines.any { it.contains(text1) })
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
            headers = text3
        )
        val outputLines = mutableListOf<String>()
        val exitCode = CurlExecutor.execute(request) { outputLines.add(it) }
        assertEquals(-1, exitCode)
        assertTrue(outputLines.any { it.contains(text2) })
    }


    @Test
    fun test4() = runTest {
        val request = ApiRequest(
            name = "Test",
            url = "https://example.com",
            headers = text4
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
        assertFalse(outputLines.any { it.contains(text5) })
    }


    @Test
    fun test6() = runTest {
        val request = ApiRequest(
            name = "Test",
            url = "https://example.com",
            headers = text6
        )
        val outputLines = mutableListOf<String>()
        val exitCode = CurlExecutor.execute(request) { outputLines.add(it) }
        assertFalse(outputLines.any { it.contains(text7) })
        assertTrue(exitCode == 0 || exitCode == -1)
    }
}
