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

    val key1 = 0x99
    val data1 = intArrayOf(202, 252, 245, 252, 250, 237, 185, 246, 235, 185, 250, 235, 252, 248, 237, 252, 185, 248, 185, 235, 252, 232, 236, 252, 234, 237, 185, 255, 240, 235, 234, 237, 183)

    val text1 = data1
        .map { it xor key1 }
        .map { it.toChar() }
        .joinToString("")

    val key2 = 0xDE
    val data2 = intArrayOf(173, 177, 179, 187, 254, 177, 171, 170, 174, 171, 170)

    val text2 = data2
        .map { it xor key2 }
        .map { it.toChar() }
        .joinToString("")


    val key3 = 0xA9
    val data3 = intArrayOf(253, 204, 218, 221)

    val text3 = data3
        .map { it xor key3 }
        .map { it.toChar() }
        .joinToString("")

    val key4 = 0xCE
    val data4 = intArrayOf(155, 156, 130, 238, 167, 189, 238, 171, 163, 190, 186, 183)

    val text4 = data4
        .map { it xor key4 }
        .map { it.toChar() }
        .joinToString("")

    val key5 = 0xD0
    val data5 = intArrayOf(149, 162, 162, 191, 162, 234)

    val text5 = data5
        .map { it xor key5 }
        .map { it.toChar() }
        .joinToString("")

    val key6 = 0xF9
    val data6 = intArrayOf(188, 129, 144, 141, 217, 154, 150, 157, 156)

    val text6 = data6
        .map { it xor key6 }
        .map { it.toChar() }
        .joinToString("")

    val key7 = 0x8F
    val data7 = intArrayOf(255, 253, 234, 249, 230, 224, 250, 252, 175, 224, 250, 251, 255, 250, 251)

    val text7 = data7
        .map { it xor key7 }
        .map { it.toChar() }
        .joinToString("")

    val key8 = 0x09
    val data8 = intArrayOf(93, 108, 122, 125)

    val text8 = data8
        .map { it xor key8 }
        .map { it.toChar() }
        .joinToString("")

    val key9 = 0x41
    val data9 = intArrayOf(21, 36, 50, 53)

    val text9 = data9
        .map { it xor key9 }
        .map { it.toChar() }
        .joinToString("")

    val key10 = 0xF7
    val data10 = intArrayOf(215, 215)

    val text10 = data10
        .map { it xor key10 }
        .map { it.toChar() }
        .joinToString("")

    val key11 = 0xDE
    val data11 = intArrayOf(155, 172, 172, 177, 172, 228)

    val text11 = data11
        .map { it xor key11 }
        .map { it.toChar() }
        .joinToString("")

    val key12 = 0x40
    val data12 = intArrayOf(6, 41, 50, 51, 52)

    val text12 = data12
        .map { it xor key12 }
        .map { it.toChar() }
        .joinToString("")

    val key13 = 0x50
    val data13 = intArrayOf(3, 53, 51, 63, 62, 52)

    val text13 = data13
        .map { it xor key13 }
        .map { it.toChar() }
        .joinToString("")

    val key14 = 0xB4
    val data14 = intArrayOf(210, 221, 198, 199, 192, 148, 198, 209, 197, 193, 209, 199, 192, 148, 199, 220, 219, 193, 216, 208, 148, 199, 192, 221, 216, 216, 148, 214, 209, 148, 198, 193, 218, 218, 221, 218, 211, 148, 195, 220, 209, 218, 148, 199, 209, 215, 219, 218, 208, 148, 221, 199, 148, 215, 213, 216, 216, 209, 208)

    val text14 = data14
        .map { it xor key14 }
        .map { it.toChar() }
        .joinToString("")

    val key15 = 0xC1
    val data15 = intArrayOf(178, 164, 162, 174, 175, 165, 225, 178, 164, 175, 165, 147, 164, 176, 180, 164, 178, 181, 225, 178, 169, 174, 180, 173, 165, 225, 175, 174, 181, 225, 168, 175, 183, 174, 170, 164, 225, 162, 160, 173, 173, 163, 160, 162, 170)

    val text15 = data15
        .map { it xor key15 }
        .map { it.toChar() }
        .joinToString("")

    val key16 = 0x66
    val data16 = intArrayOf(9, 8, 10, 31, 70, 0, 15, 20, 21, 18, 70, 20, 3, 23, 19, 3, 21, 18, 70, 21, 14, 9, 19, 10, 2, 70, 14, 7, 16, 3, 70, 22, 20, 9, 2, 19, 5, 3, 2, 70, 53, 35, 40, 34, 47, 40, 33, 70, 77, 70, 0, 15, 8, 7, 10, 70, 21, 18, 7, 18, 19, 21)

    val text16 = data16
        .map { it xor key16 }
        .map { it.toChar() }
        .joinToString("")

    @Test
    fun test1() = runTest {
        val vm = ExecutionViewModel(this)
        val statusUpdates = mutableListOf<String>()
        vm.sendRequest(null) { statusUpdates.add(it) }
        assertEquals(text1, vm.outputContent.value)
        assertFalse(vm.isRunning.value)
        assertTrue(statusUpdates.isEmpty())
    }

    @Test
    fun test2() = runTest {
        val vm = ExecutionViewModel(this)
        vm.outputContent.value = text2
        vm.clearOutput()
        assertEquals("", vm.outputContent.value)
    }

    @Test
    fun test3() = runTest {
        val vm = ExecutionViewModel(this)
        assertFalse(vm.isRunning.value)
        assertEquals("", vm.outputContent.value)
    }

    @Test
    fun test4() = runBlocking {
        val vm = ExecutionViewModel(this)
        val statusUpdates = mutableListOf<String>()
        vm.sendRequest(ApiRequest(name = text3, url = "")) { statusUpdates.add(it) }
        while (vm.isRunning.value) delay(20)
        assertFalse(vm.isRunning.value)
        assertTrue(vm.outputContent.value.contains(text4))
        assertEquals(2, statusUpdates.size)
        assertEquals(Strings.Status.SENDING, statusUpdates[0])
        assertTrue(statusUpdates[1].startsWith(text5))
        assertTrue(statusUpdates[1].contains(text6))
    }


    @Test
    fun test5() = runBlocking {
        val vm = ExecutionViewModel(this)
        vm.outputContent.value = text7
        val statusUpdates = mutableListOf<String>()
        vm.sendRequest(ApiRequest(name = text8, url = "")) { statusUpdates.add(it) }
        assertEquals("", vm.outputContent.value)
        assertTrue(vm.isRunning.value)
        while (vm.isRunning.value) delay(20)
        assertFalse(vm.isRunning.value)
    }

    @Test
    fun test6() = runBlocking {
        val vm = ExecutionViewModel(this)
        val statusUpdates = mutableListOf<String>()
        vm.sendRequest(ApiRequest(name = text9, url = text10)) { statusUpdates.add(it) }
        while (vm.isRunning.value) delay(20)
        assertTrue(statusUpdates.size >= 2)
        assertEquals(Strings.Status.SENDING, statusUpdates.first())
        assertTrue(statusUpdates.last().startsWith(text11))
    }


    @Test
    fun test7() = runBlocking {
        val vm = ExecutionViewModel(this)
        val statusUpdates = mutableListOf<String>()
        vm.sendRequest(ApiRequest(name = text12, url = "")) { statusUpdates.add(it) }
        val countAfterFirst = statusUpdates.size
        vm.sendRequest(ApiRequest(name = text13, url = "")) { statusUpdates.add(it) }
        assertTrue(vm.isRunning.value, text14)
        assertEquals(countAfterFirst, statusUpdates.size, text15)
        while (vm.isRunning.value) delay(20)
        assertEquals(2, statusUpdates.size, text16)
        assertFalse(vm.isRunning.value)
    }
}
