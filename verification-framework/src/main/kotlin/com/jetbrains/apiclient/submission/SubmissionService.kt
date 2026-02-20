package com.jetbrains.apiclient.submission

import com.jetbrains.apiclient.verification.VerificationResult
import com.jetbrains.apiclient.verification.VerificationRunner
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.zip.ZipOutputStream

object SubmissionService {

    private val httpClient = HttpClient.newBuilder().build()
    private val json = Json { ignoreUnknownKeys = true }

    fun triggerNetworkPreflight() {
        try {
            val uri = URI.create(SubmissionConfig.baseUrl())
            val request = HttpRequest.newBuilder(uri).GET().build()
            httpClient.send(request, HttpResponse.BodyHandlers.discarding())
        } catch (_: Exception) { }
    }

    fun canCreateSubmissionZip(root: Path): Result<Unit> {
        val srcMain = root.resolve("src").resolve("main")
        return if (Files.isDirectory(srcMain)) Result.success(Unit)
        else Result.failure(IllegalStateException("Missing src/main directory"))
    }

    fun createSubmissionZip(root: Path): ByteArray {
        val buffer = java.io.ByteArrayOutputStream()
        ZipOutputStream(buffer).use { zos ->
            listOf(
                root.resolve("src").resolve("main"),
                root.resolve("build")
            ).filter { Files.exists(it) }.forEach { dir ->
                Files.walkFileTree(dir, object : SimpleFileVisitor<Path>() {
                    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                        val entryName = root.relativize(file).toString().replace('\\', '/')
                        zos.putNextEntry(java.util.zip.ZipEntry(entryName))
                        Files.copy(file, zos)
                        zos.closeEntry()
                        return FileVisitResult.CONTINUE
                    }
                })
            }
        }
        return buffer.toByteArray()
    }

    fun runVerification(): List<VerificationResult>? = VerificationRunner.runDefault()

    fun submit(participantName: String, zipBytes: ByteArray, results: List<VerificationResult>): Result<Unit> {
        return try {
            val boundary = "----FormBoundary${System.nanoTime()}"
            val resultsJson = json.encodeToString(results)
            val body = buildList {
                add("--$boundary\r\nContent-Disposition: form-data; name=\"name\"\r\n\r\n$participantName\r\n")
                add("--$boundary\r\nContent-Disposition: form-data; name=\"results\"\r\nContent-Type: application/json\r\n\r\n$resultsJson\r\n")
                add("--$boundary\r\nContent-Disposition: form-data; name=\"zip\"; filename=\"submission.zip\"\r\nContent-Type: application/zip\r\n\r\n")
            }
            val bodyPrefix = body.joinToString("").toByteArray(Charsets.UTF_8)
            val bodySuffix = "\r\n--$boundary--\r\n".toByteArray(Charsets.UTF_8)
            val fullBody = bodyPrefix + zipBytes + bodySuffix

            val request = HttpRequest.newBuilder(URI.create(SubmissionConfig.submissionEndpoint()))
                .header("Content-Type", "multipart/form-data; boundary=$boundary")
                .POST(HttpRequest.BodyPublishers.ofByteArray(fullBody))
                .build()
            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
            if (response.statusCode() in 200..299) Result.success(Unit)
            else Result.failure(RuntimeException("HTTP ${response.statusCode()}: ${response.body()?.take(200) ?: ""}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
