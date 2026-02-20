package com.jetbrains.apiclient.viewmodel

import com.jetbrains.apiclient.model.ApiRequest
import com.jetbrains.apiclient.resources.Strings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExecutionViewModelTest {

    @Test
    fun `sendRequest with null shows message and does not run`() = runTest {
        val vm = ExecutionViewModel(this)
        val statusUpdates = mutableListOf<String>()
        vm.sendRequest(null) { statusUpdates.add(it) }
        assertEquals("Select or create a request first.", vm.outputContent.value)
        assertFalse(vm.isRunning.value)
        assertTrue(statusUpdates.isEmpty())
    }

    @Test
    fun `clearOutput clears outputContent`() = runTest {
        val vm = ExecutionViewModel(this)
        vm.outputContent.value = "some output"
        vm.clearOutput()
        assertEquals("", vm.outputContent.value)
    }

    @Test
    fun `initial state is not running and empty output`() = runTest {
        val vm = ExecutionViewModel(this)
        assertFalse(vm.isRunning.value)
        assertEquals("", vm.outputContent.value)
    }

    @Test
    fun `sendRequest with blank URL completes with error status and output`() = runBlocking {
        val vm = ExecutionViewModel(this)
        val statusUpdates = mutableListOf<String>()
        vm.sendRequest(ApiRequest(name = "Test", url = "")) { statusUpdates.add(it) }
        while (vm.isRunning.value) delay(20)
        assertFalse(vm.isRunning.value)
        assertTrue(vm.outputContent.value.contains("URL is empty"))
        assertEquals(2, statusUpdates.size)
        assertEquals(Strings.Status.SENDING, statusUpdates[0])
        assertTrue(statusUpdates[1].startsWith("Error:"))
        assertTrue(statusUpdates[1].contains("Exit code"))
    }

    @Test
    fun `sendRequest clears output when starting`() = runBlocking {
        val vm = ExecutionViewModel(this)
        vm.outputContent.value = "previous output"
        val statusUpdates = mutableListOf<String>()
        vm.sendRequest(ApiRequest(name = "Test", url = "")) { statusUpdates.add(it) }
        assertEquals("", vm.outputContent.value)
        assertTrue(vm.isRunning.value)
        while (vm.isRunning.value) delay(20)
        assertFalse(vm.isRunning.value)
    }

    @Test
    fun `sendRequest invokes onStatusUpdate with SENDING then final status`() = runBlocking {
        val vm = ExecutionViewModel(this)
        val statusUpdates = mutableListOf<String>()
        vm.sendRequest(ApiRequest(name = "Test", url = "  ")) { statusUpdates.add(it) }
        while (vm.isRunning.value) delay(20)
        assertTrue(statusUpdates.size >= 2)
        assertEquals(Strings.Status.SENDING, statusUpdates.first())
        assertTrue(statusUpdates.last().startsWith("Error:"))
    }

    @Test
    fun `sendRequest when already running does not start second execution`() = runBlocking {
        val vm = ExecutionViewModel(this)
        val statusUpdates = mutableListOf<String>()
        vm.sendRequest(ApiRequest(name = "First", url = "")) { statusUpdates.add(it) }
        val countAfterFirst = statusUpdates.size
        vm.sendRequest(ApiRequest(name = "Second", url = "")) { statusUpdates.add(it) }
        assertTrue(vm.isRunning.value, "first request should still be running when second is called")
        assertEquals(countAfterFirst, statusUpdates.size, "second sendRequest should not invoke callback")
        while (vm.isRunning.value) delay(20)
        assertEquals(2, statusUpdates.size, "only first request should have produced SENDING + final status")
        assertFalse(vm.isRunning.value)
    }
}
