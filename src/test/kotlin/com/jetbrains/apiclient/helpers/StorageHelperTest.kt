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

    @TempDir
    lateinit var tempDir: File

    @AfterEach
    fun resetTestDir() {
        StorageHelper.testDataDir = null
    }

    @Test
    fun `loadRequests returns empty list when file does not exist`() = runTest {
        StorageHelper.testDataDir = tempDir
        val result = StorageHelper.loadRequests()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `loadRequests returns empty list when file is empty`() = runTest {
        StorageHelper.testDataDir = tempDir
        File(tempDir, "data.json").writeText("")
        val result = StorageHelper.loadRequests()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `loadRequests returns empty list when file is blank`() = runTest {
        StorageHelper.testDataDir = tempDir
        File(tempDir, "data.json").writeText("   \n\t  ")
        val result = StorageHelper.loadRequests()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `loadRequests returns empty list on invalid JSON`() = runTest {
        StorageHelper.testDataDir = tempDir
        File(tempDir, "data.json").writeText("not json at all")
        val result = StorageHelper.loadRequests()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `save and load round-trip preserves requests`() = runTest {
        StorageHelper.testDataDir = tempDir
        val requests = listOf(
            ApiRequest(name = "Get users", url = "https://api.example.com/users", method = HttpMethod.GET),
            ApiRequest(name = "Post item", url = "https://api.example.com/items", method = HttpMethod.POST, body = "{}")
        )
        StorageHelper.saveRequests(requests)
        val loaded = StorageHelper.loadRequests()
        assertEquals(2, loaded.size)
        assertEquals("Get users", loaded[0].name)
        assertEquals("https://api.example.com/users", loaded[0].url)
        assertEquals(HttpMethod.GET, loaded[0].method)
        assertEquals("Post item", loaded[1].name)
        assertEquals(HttpMethod.POST, loaded[1].method)
        assertEquals("{}", loaded[1].body)
    }

    @Test
    fun `save and load round-trip preserves empty list`() = runTest {
        StorageHelper.testDataDir = tempDir
        StorageHelper.saveRequests(emptyList())
        val loaded = StorageHelper.loadRequests()
        assertTrue(loaded.isEmpty())
    }

    @Test
    fun `load ignores unknown keys in JSON`() = runTest {
        StorageHelper.testDataDir = tempDir
        val json = """[
            {"id":"a","name":"R1","url":"","method":"GET","headers":"","body":"","unknownField":123}
        ]"""
        File(tempDir, "data.json").writeText(json)
        val loaded = StorageHelper.loadRequests()
        assertEquals(1, loaded.size)
        assertEquals("R1", loaded[0].name)
    }
}
