package com.jetbrains.apiclient.viewmodel

import com.jetbrains.apiclient.helpers.StorageHelper
import com.jetbrains.apiclient.model.ApiRequest
import com.jetbrains.apiclient.model.HttpMethod
import com.jetbrains.apiclient.resources.Strings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class RequestsViewModelTest {

    @TempDir
    lateinit var tempDir: File

    @AfterEach
    fun reset() {
        StorageHelper.testDataDir = null
        StorageHelper.testDispatcher = null
    }

    private fun createViewModel(): RequestsViewModel = runBlocking {
        StorageHelper.testDataDir = tempDir
        val scope = this
        val vm = RequestsViewModel(scope)
        withTimeoutOrNull(2000) {
            while (vm.requests.first().isEmpty()) {
                kotlinx.coroutines.delay(50)
            }
        }
        vm
    }

    @Test
    fun test1() = runBlocking {
        StorageHelper.testDataDir = tempDir
        val vm = RequestsViewModel(this)
        withTimeoutOrNull(2000) {
            while (vm.requests.first().isEmpty()) {
                kotlinx.coroutines.delay(50)
            }
        }
        val requests = vm.requests.first()
        assertEquals(1, requests.size)
        assertEquals("New request", requests[0].name)
        assertEquals("New request", vm.selectedRequest.value?.name)
    }

    @Test
    fun test2() = runBlocking {
        val vm = createViewModel()
        vm.showNewRequestDialog()
        vm.addRequest("  My Request  ")
        kotlinx.coroutines.delay(100)
        assertEquals(2, vm.requests.first().size)
        assertEquals("My Request", vm.selectedRequest.value?.name)
        assertFalse(vm.showNewRequestDialog.value)
    }

    @Test
    fun test3() = runBlocking {
        val vm = createViewModel()
        val initialCount = vm.requests.first().size
        vm.addRequest("")
        vm.addRequest("   ")
        kotlinx.coroutines.delay(100)
        assertEquals(initialCount, vm.requests.first().size)
    }

    @Test
    fun test4() = runBlocking {
        val vm = createViewModel()
        val req = vm.requests.first().first()
        vm.showRenameRequestDialog(req)
        vm.renameRequest(req, "  Renamed  ")
        kotlinx.coroutines.delay(100)
        assertEquals("Renamed", vm.requests.first().first().name)
        assertEquals("Renamed", vm.selectedRequest.value?.name)
        assertNull(vm.showRenameRequestDialog.value)
    }

    @Test
    fun test5() = runBlocking {
        val vm = createViewModel()
        val req = vm.requests.first().first()
        vm.renameRequest(req, "")
        vm.renameRequest(req, "   ")
        kotlinx.coroutines.delay(100)
        assertEquals("New request", vm.requests.first().first().name)
    }

    @Test
    fun test6() = runBlocking {
        val vm = createViewModel()
        vm.addRequest("Second")
        kotlinx.coroutines.delay(100)
        val toDelete = vm.requests.first().first { it.name == "New request" }
        vm.showDeleteRequestDialog(toDelete)
        vm.deleteRequest(toDelete)
        kotlinx.coroutines.delay(100)
        assertEquals(1, vm.requests.first().size)
        assertEquals("Second", vm.selectedRequest.value?.name)
        assertNull(vm.showDeleteRequestDialog.value)
        assertEquals(Strings.Status.READY, vm.statusMessage.value)
    }

    @Test
    fun test7() = runBlocking {
        val vm = createViewModel()
        vm.addRequest("A")
        vm.addRequest("B")
        kotlinx.coroutines.delay(100)
        val a = vm.requests.first().find { it.name == "A" }!!
        vm.selectRequest(a)
        assertEquals("A", vm.selectedRequest.value?.name)
        vm.selectRequest(null)
        assertNull(vm.selectedRequest.value)
    }

    @Test
    fun test8() = runBlocking {
        val vm = createViewModel()
        vm.updateCurrentRequest(
            url = "https://api.test.com",
            method = HttpMethod.POST,
            headers = "{}",
            body = "{\"x\":1}"
        )
        kotlinx.coroutines.delay(100)
        val updated = vm.requests.first().first()
        assertEquals("https://api.test.com", updated.url)
        assertEquals(HttpMethod.POST, updated.method)
        assertEquals("{}", updated.headers)
        assertEquals("{\"x\":1}", updated.body)
        assertEquals(updated.id, vm.selectedRequest.value?.id)
    }

    @Test
    fun test9() = runBlocking {
        val vm = createViewModel()
        vm.selectRequest(null)
        vm.updateCurrentRequest(url = "https://bad.com")
        kotlinx.coroutines.delay(100)
        assertNotEquals("https://bad.com", vm.requests.first().firstOrNull()?.url)
    }

    @Test
    fun test10() = runBlocking {
        val vm = createViewModel()
        val req = vm.requests.first().first()
        assertFalse(vm.showNewRequestDialog.value)
        vm.showNewRequestDialog()
        assertTrue(vm.showNewRequestDialog.value)
        vm.dismissNewRequestDialog()
        assertFalse(vm.showNewRequestDialog.value)

        assertNull(vm.showRenameRequestDialog.value)
        vm.showRenameRequestDialog(req)
        assertEquals(req.id, vm.showRenameRequestDialog.value?.id)
        vm.dismissRenameRequestDialog()
        assertNull(vm.showRenameRequestDialog.value)

        assertNull(vm.showDeleteRequestDialog.value)
        vm.showDeleteRequestDialog(req)
        assertEquals(req.id, vm.showDeleteRequestDialog.value?.id)
        vm.dismissDeleteRequestDialog()
        assertNull(vm.showDeleteRequestDialog.value)
    }

    @Test
    fun test11() = runBlocking {
        StorageHelper.testDataDir = tempDir
        StorageHelper.saveRequests(
            listOf(
                ApiRequest(name = "Saved 1", url = "https://a.com"),
                ApiRequest(name = "Saved 2", url = "https://b.com")
            )
        )
        kotlinx.coroutines.delay(100)
        val vm = RequestsViewModel(this)
        withTimeoutOrNull(2000) {
            while (vm.requests.first().size < 2) {
                kotlinx.coroutines.delay(50)
            }
        }
        val requests = vm.requests.first()
        assertEquals(2, requests.size)
        assertTrue(requests.any { it.name == "Saved 1" })
        assertTrue(requests.any { it.name == "Saved 2" })
    }
}
