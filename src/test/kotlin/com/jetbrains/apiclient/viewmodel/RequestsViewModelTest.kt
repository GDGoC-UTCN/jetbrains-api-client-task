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

    val key1 = 0x32
    val data1 = intArrayOf(124, 87, 69, 18, 64, 87, 67, 71, 87, 65, 70)

    val text1 = data1
        .map { it xor key1 }
        .map { it.toChar() }
        .joinToString("")

    val key2 = 0xE6
    val data2 = intArrayOf(168, 131, 145, 198, 148, 131, 151, 147, 131, 149, 146)

    val text2 = data2
        .map { it xor key2 }
        .map { it.toChar() }
        .joinToString("")

    val key3 = 0x99
    val data3 = intArrayOf(185, 185, 212, 224, 185, 203, 252, 232, 236, 252, 234, 237, 185, 185)

    val text3 = data3
        .map { it xor key3 }
        .map { it.toChar() }
        .joinToString("")

    val key4 = 0x5F
    val data4 = intArrayOf(18, 38, 127, 13, 58, 46, 42, 58, 44, 43)

    val text4 = data4
        .map { it xor key4 }
        .map { it.toChar() }
        .joinToString("")

    val key5 = 0xE4
    val data5 = intArrayOf(196, 196, 196)

    val text5 = data5
        .map { it xor key5 }
        .map { it.toChar() }
        .joinToString("")


    val key6 = 0xD8
    val data6 = intArrayOf(248, 248, 138, 189, 182, 185, 181, 189, 188, 248, 248)

    val text6 = data6
        .map { it xor key6 }
        .map { it.toChar() }
        .joinToString("")


    val key7 = 0x46
    val data7 = intArrayOf(20, 35, 40, 39, 43, 35, 34)

    val text7 = data7
        .map { it xor key7 }
        .map { it.toChar() }
        .joinToString("")

    val key8 = 0x48
    val data8 = intArrayOf(26, 45, 38, 41, 37, 45, 44)

    val text8 = data8
        .map { it xor key8 }
        .map { it.toChar() }
        .joinToString("")

    val key9 = 0x06
    val data9 = intArrayOf(72, 99, 113, 38, 116, 99, 119, 115, 99, 117, 114)

    val text9 = data9
        .map { it xor key9 }
        .map { it.toChar() }
        .joinToString("")

    val key10 = 0x41
    val data10 = intArrayOf(97, 97, 97)

    val text10 = data10
        .map { it xor key10 }
        .map { it.toChar() }
        .joinToString("")


    val key11 = 0x46
    val data11 = intArrayOf(21, 35, 37, 41, 40, 34)

    val text11 = data11
        .map { it xor key11 }
        .map { it.toChar() }
        .joinToString("")

    val key12 = 0xAC
    val data12 = intArrayOf(226, 201, 219, 140, 222, 201, 221, 217, 201, 223, 216)

    val text12 = data12
        .map { it xor key12 }
        .map { it.toChar() }
        .joinToString("")

    val key13 = 0x14
    val data13 = intArrayOf(71, 113, 119, 123, 122, 112)

    val text13 = data13
        .map { it xor key13 }
        .map { it.toChar() }
        .joinToString("")


    val key14 = 0x40
    val data14 = intArrayOf(1)

    val text14 = data14
        .map { it xor key14 }
        .map { it.toChar() }
        .joinToString("")

    val key15 = 0xB0
    val data15 = intArrayOf(241)

    val text15 = data15
        .map { it xor key15 }
        .map { it.toChar() }
        .joinToString("")

    val key16 = 0xF7
    val data16 = intArrayOf(159, 131, 131, 135, 132, 205, 216, 216, 150, 135, 158, 217, 131, 146, 132, 131, 217, 148, 152, 154)

    val text16 = data16
        .map { it xor key16 }
        .map { it.toChar() }
        .joinToString("")

    val key17 = 0x44
    val data17 = intArrayOf(44, 48, 48, 52, 55, 126, 107, 107, 37, 52, 45, 106, 48, 33, 55, 48, 106, 39, 43, 41)

    val text17 = data17
        .map { it xor key17 }
        .map { it.toChar() }
        .joinToString("")

    val key18 = 0x6B
    val data18 = intArrayOf(16, 22)

    val text18 = data18
        .map { it xor key18 }
        .map { it.toChar() }
        .joinToString("")

    val key19 = 0x11
    val data19 = intArrayOf(106, 51, 105, 51, 43, 32, 108)

    val text19 = data19
        .map { it xor key19 }
        .map { it.toChar() }
        .joinToString("")

    val key20 = 0x8D
    val data20 = intArrayOf(246, 240)

    val text20 = data20
        .map { it xor key20 }
        .map { it.toChar() }
        .joinToString("")

    val key21 = 0xE7
    val data21 = intArrayOf(156, 197, 159, 197, 221, 214, 154)

    val text21 = data21
        .map { it xor key21 }
        .map { it.toChar() }
        .joinToString("")


    val key22 = 0x28
    val data22 = intArrayOf(64, 92, 92, 88, 91, 18, 7, 7, 74, 73, 76, 6, 75, 71, 69)

    val text22 = data22
        .map { it xor key22 }
        .map { it.toChar() }
        .joinToString("")

    val key23 = 0x63
    val data23 = intArrayOf(11, 23, 23, 19, 16, 89, 76, 76, 1, 2, 7, 77, 0, 12, 14)

    val text23 = data23
        .map { it xor key23 }
        .map { it.toChar() }
        .joinToString("")

    val key24 = 0x3F
    val data24 = intArrayOf(108, 94, 73, 90, 91, 31, 14)

    val text24 = data24
        .map { it xor key24 }
        .map { it.toChar() }
        .joinToString("")

    val key25 = 0xF5
    val data25 = intArrayOf(166, 148, 131, 144, 145, 213, 199)

    val text25 = data25
        .map { it xor key25 }
        .map { it.toChar() }
        .joinToString("")

    val key26 = 0x22
    val data26 = intArrayOf(74, 86, 86, 82, 81, 24, 13, 13, 67, 12, 65, 77, 79)

    val text26 = data26
        .map { it xor key26 }
        .map { it.toChar() }
        .joinToString("")

    val key27 = 0x26
    val data27 = intArrayOf(78, 82, 82, 86, 85, 28, 9, 9, 68, 8, 69, 73, 75)

    val text27 = data27
        .map { it xor key27 }
        .map { it.toChar() }
        .joinToString("")

    val key28 = 0x65
    val data28 = intArrayOf(54, 4, 19, 0, 1, 69, 84)

    val text28 = data28
        .map { it xor key28 }
        .map { it.toChar() }
        .joinToString("")

    val key29 = 0x2F
    val data29 = intArrayOf(124, 78, 89, 74, 75, 15, 29)

    val text29 = data29
        .map { it xor key29 }
        .map { it.toChar() }
        .joinToString("")

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
        assertEquals(text1, requests[0].name)
        assertEquals(text2, vm.selectedRequest.value?.name)
    }

    @Test
    fun test2() = runBlocking {
        val vm = createViewModel()
        vm.showNewRequestDialog()
        vm.addRequest(text3)
        kotlinx.coroutines.delay(100)
        assertEquals(2, vm.requests.first().size)
        assertEquals(text4, vm.selectedRequest.value?.name)
        assertFalse(vm.showNewRequestDialog.value)
    }

    @Test
    fun test3() = runBlocking {
        val vm = createViewModel()
        val initialCount = vm.requests.first().size
        vm.addRequest("")
        vm.addRequest(text5)
        kotlinx.coroutines.delay(100)
        assertEquals(initialCount, vm.requests.first().size)
    }

    @Test
    fun test4() = runBlocking {
        val vm = createViewModel()
        val req = vm.requests.first().first()
        vm.showRenameRequestDialog(req)
        vm.renameRequest(req, text6)
        kotlinx.coroutines.delay(100)
        assertEquals(text7, vm.requests.first().first().name)
        assertEquals(text8, vm.selectedRequest.value?.name)
        assertNull(vm.showRenameRequestDialog.value)
    }

    @Test
    fun test5() = runBlocking {
        val vm = createViewModel()
        val req = vm.requests.first().first()
        vm.renameRequest(req, "")
        vm.renameRequest(req, text10)
        kotlinx.coroutines.delay(100)
        assertEquals(text9, vm.requests.first().first().name)
    }

    @Test
    fun test6() = runBlocking {
        val vm = createViewModel()
        vm.addRequest(text11)
        kotlinx.coroutines.delay(100)
        val toDelete = vm.requests.first().first { it.name == text12 }
        vm.showDeleteRequestDialog(toDelete)
        vm.deleteRequest(toDelete)
        kotlinx.coroutines.delay(100)
        assertEquals(1, vm.requests.first().size)
        assertEquals(text13, vm.selectedRequest.value?.name)
        assertNull(vm.showDeleteRequestDialog.value)
        assertEquals(Strings.Status.READY, vm.statusMessage.value)
    }

    @Test
    fun test7() = runBlocking {
        val vm = createViewModel()
        vm.addRequest("A")
        vm.addRequest("B")
        kotlinx.coroutines.delay(100)
        val a = vm.requests.first().find { it.name == text15 }!!
        vm.selectRequest(a)
        assertEquals(text14, vm.selectedRequest.value?.name)
        vm.selectRequest(null)
        assertNull(vm.selectedRequest.value)
    }

    @Test
    fun test8() = runBlocking {
        val vm = createViewModel()
        vm.updateCurrentRequest(
            url = text16,
            method = HttpMethod.POST,
            headers = text18,
            body = text19
        )
        kotlinx.coroutines.delay(100)
        val updated = vm.requests.first().first()
        assertEquals(text17, updated.url)
        assertEquals(HttpMethod.POST, updated.method)
        assertEquals(text20, updated.headers)
        assertEquals(text21, updated.body)
        assertEquals(updated.id, vm.selectedRequest.value?.id)
    }

    @Test
    fun test9() = runBlocking {
        val vm = createViewModel()
        vm.selectRequest(null)
        vm.updateCurrentRequest(url = text22)
        kotlinx.coroutines.delay(100)
        assertNotEquals(text23, vm.requests.first().firstOrNull()?.url)
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
                ApiRequest(name = text24, url = text26),
                ApiRequest(name = text25, url = text27)
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
        assertTrue(requests.any { it.name == text28 })
        assertTrue(requests.any { it.name == text29 })
    }
}
