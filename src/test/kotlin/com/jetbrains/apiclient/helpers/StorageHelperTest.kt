package com.jetbrains.apiclient.helpers

import com.jetbrains.apiclient.model.ApiRequest
import com.jetbrains.apiclient.model.HttpMethod
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class StorageHelperTest {
    val key1 = 0x60
    val data1 = intArrayOf(4, 1, 20, 1, 78, 10, 19, 15, 14)

    val text1 = data1
        .map { it xor key1 }
        .map { it.toChar() }
        .joinToString("")


    val key2 = 0xD6
    val data2 = intArrayOf(178, 183, 162, 183, 248, 188, 165, 185, 184)

    val text2 = data2
        .map { it xor key2 }
        .map { it.toChar() }
        .joinToString("")

    val key3 = 0x3F
    val data3 = intArrayOf(31, 31, 31, 53, 54, 31, 31)

    val text3 = data3
        .map { it xor key3 }
        .map { it.toChar() }
        .joinToString("")

    val key4 = 0x6B
    val data4 = intArrayOf(15, 10, 31, 10, 69, 1, 24, 4, 5)

    val text4 = data4
        .map { it xor key4 }
        .map { it.toChar() }
        .joinToString("")

    val key5 = 0xC5
    val data5 = intArrayOf(171, 170, 177, 229, 175, 182, 170, 171, 229, 164, 177, 229, 164, 169, 169)

    val text5 = data5
        .map { it xor key5 }
        .map { it.toChar() }
        .joinToString("")

    val key6 = 0x4A
    val data6 = intArrayOf(13, 47, 62, 106, 63, 57, 47, 56, 57)

    val text6 = data6
        .map { it xor key6 }
        .map { it.toChar() }
        .joinToString("")

    val key7 = 0x68
    val data7 = intArrayOf(0, 28, 28, 24, 27, 82, 71, 71, 9, 24, 1, 70, 13, 16, 9, 5, 24, 4, 13, 70, 11, 7, 5, 71, 29, 27, 13, 26, 27)

    val text7 = data7
        .map { it xor key7 }
        .map { it.toChar() }
        .joinToString("")

    val key8 = 0x23
    val data8 = intArrayOf(115, 76, 80, 87, 3, 74, 87, 70, 78)

    val text8 = data8
        .map { it xor key8 }
        .map { it.toChar() }
        .joinToString("")

    val key9 = 0xB8
    val data9 = intArrayOf(208, 204, 204, 200, 203, 130, 151, 151, 217, 200, 209, 150, 221, 192, 217, 213, 200, 212, 221, 150, 219, 215, 213, 151, 209, 204, 221, 213, 203)
    val text9 = data9
        .map { it xor key9 }
        .map { it.toChar() }
        .joinToString("")

    val key10 = 0x44
    val data10 = intArrayOf(3, 33, 48, 100, 49, 55, 33, 54, 55)

    val text10 = data10
        .map { it xor key10 }
        .map { it.toChar() }
        .joinToString("")


    val key11 = 0x8A
    val data11 = intArrayOf(226, 254, 254, 250, 249, 176, 165, 165, 235, 250, 227, 164, 239, 242, 235, 231, 250, 230, 239, 164, 233, 229, 231, 165, 255, 249, 239, 248, 249)

    val text11 = data11
        .map { it xor key11 }
        .map { it.toChar() }
        .joinToString("")

    val key12 = 0x9B
    val data12 = intArrayOf(203, 244, 232, 239, 187, 242, 239, 254, 246)

    val text12 = data12
        .map { it xor key12 }
        .map { it.toChar() }
        .joinToString("")

    val key13 = 0xAE
    val data13 = intArrayOf(245, 164, 142, 142, 142, 142, 142, 142, 142, 142, 142, 142, 142, 142, 213, 140, 199, 202, 140, 148, 140, 207, 140, 130, 140, 192, 207, 195, 203, 140, 148, 140, 252, 159, 140, 130, 140, 219, 220, 194, 140, 148, 140, 140, 130, 140, 195, 203, 218, 198, 193, 202, 140, 148, 140, 233, 235, 250, 140, 130, 140, 198, 203, 207, 202, 203, 220, 221, 140, 148, 140, 140, 130, 140, 204, 193, 202, 215, 140, 148, 140, 140, 130, 140, 219, 192, 197, 192, 193, 217, 192, 232, 199, 203, 194, 202, 140, 148, 159, 156, 157, 211, 164, 142, 142, 142, 142, 142, 142, 142, 142, 243)

    val text13 = data13
        .map { it xor key13 }
        .map { it.toChar() }
        .joinToString("")

    val key14 = 0x5F
    val data14 = intArrayOf(59, 62, 43, 62, 113, 53, 44, 48, 49)

    val text14 = data14
        .map { it xor key14 }
        .map { it.toChar() }
        .joinToString("")

    val key15 = 0x2E
    val data15 = intArrayOf(124, 31)

    val text15 = data15
        .map { it xor key15 }
        .map { it.toChar() }
        .joinToString("")
    @TempDir
    lateinit var tempDir: File

    @AfterEach
    fun resetTestDir() {
        StorageHelper.testDataDir = null
    }

    @Test
    fun test1() = runTest {
        StorageHelper.testDataDir = tempDir
        val result = StorageHelper.loadRequests()
        assertTrue(result.isEmpty())
    }

    @Test
    fun test2() = runTest {
        StorageHelper.testDataDir = tempDir
        File(tempDir, text1).writeText("")
        val result = StorageHelper.loadRequests()
        assertTrue(result.isEmpty())
    }


    @Test
    fun test3() = runTest {
        StorageHelper.testDataDir = tempDir
        File(tempDir, text2).writeText(text3)
        val result = StorageHelper.loadRequests()
        assertTrue(result.isEmpty())
    }

    @Test
    fun test4() = runTest {
        StorageHelper.testDataDir = tempDir
        File(tempDir, text4).writeText(text5)
        val result = StorageHelper.loadRequests()
        assertTrue(result.isEmpty())
    }

    @Test
    fun test5() = runTest {
        StorageHelper.testDataDir = tempDir
        val requests = listOf(
            ApiRequest(name = text6, url = text7, method = HttpMethod.GET),
            ApiRequest(name = text8, url = text9, method = HttpMethod.POST, body = "{}")
        )
        StorageHelper.saveRequests(requests)
        val loaded = StorageHelper.loadRequests()
        assertEquals(2, loaded.size)
        assertEquals(text10, loaded[0].name)
        assertEquals(text11, loaded[0].url)
        assertEquals(HttpMethod.GET, loaded[0].method)
        assertEquals(text12, loaded[1].name)
        assertEquals(HttpMethod.POST, loaded[1].method)
        assertEquals("{}", loaded[1].body)
    }

    @Test
    fun test6() = runTest {
        StorageHelper.testDataDir = tempDir
        StorageHelper.saveRequests(emptyList())
        val loaded = StorageHelper.loadRequests()
        assertTrue(loaded.isEmpty())
    }

    @Test
    fun test7() = runTest {
        StorageHelper.testDataDir = tempDir
        val json = text13
        File(tempDir, text14).writeText(json)
        val loaded = StorageHelper.loadRequests()
        assertEquals(1, loaded.size)
        assertEquals(text15, loaded[0].name)
    }
}
