package com.jetbrains.apiclient.helpers

import com.jetbrains.apiclient.constants.Constants
import com.jetbrains.apiclient.model.ApiRequest
import com.jetbrains.apiclient.model.ApiRequestDto
import com.jetbrains.apiclient.model.toApiRequest
import com.jetbrains.apiclient.model.toDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object StorageHelper {
    // TODO: Figure out a way to stub these without this hack :(
    var testDataDir: File? = null
    var testDispatcher: CoroutineDispatcher? = null

    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    private fun dataFile(): File {
        val workingDir = testDataDir ?: File(System.getProperty("user.dir"))
        return File(workingDir, Constants.DATA_FILE)
    }

    private fun ioDispatcher(): CoroutineDispatcher = testDispatcher ?: Dispatchers.IO

    suspend fun loadRequests(): List<ApiRequest> = withContext(ioDispatcher()) {
        val file = dataFile()
        if (!file.exists()) return@withContext emptyList()
        try {
            val text = file.readText(Charsets.UTF_8)
            if (text.isBlank()) return@withContext emptyList()
            val list = json.decodeFromString<List<ApiRequestDto>>(text)
            list.map { it.toApiRequest() }
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun saveRequests(requests: List<ApiRequest>) = withContext(ioDispatcher()) {
        val dtos = requests.map { it.toDto() }
        dataFile().writeText(json.encodeToString(dtos), Charsets.UTF_8)
    }
}
