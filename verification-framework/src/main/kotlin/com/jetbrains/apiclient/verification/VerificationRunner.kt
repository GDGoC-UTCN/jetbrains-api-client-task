package com.jetbrains.apiclient.verification

import org.junit.platform.engine.discovery.DiscoverySelectors
import org.junit.platform.launcher.Launcher
import org.junit.platform.launcher.LauncherDiscoveryRequest
import org.junit.platform.launcher.core.LauncherConfig
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

object VerificationRunner {

    private const val VERIFICATION_JAR_RESOURCE = "verification/verification.jar"

    fun findVerificationJarPath(): Path? {
        val asResource = javaClass.getResource("/$VERIFICATION_JAR_RESOURCE") ?: return findExternalJar()
        if (asResource.protocol == "file") {
            return Paths.get(asResource.toURI())
        }
        if (asResource.protocol == "jar") {
            javaClass.getResourceAsStream("/$VERIFICATION_JAR_RESOURCE")?.use { input ->
                val temp = Files.createTempFile("verification", ".jar")
                temp.toFile().deleteOnExit()
                Files.copy(input, temp, StandardCopyOption.REPLACE_EXISTING)
                return temp
            }
        }
        return findExternalJar()
    }

    private fun findExternalJar(): Path? {
        val cwd = Paths.get(System.getProperty("user.dir"))
        val nextToApp = cwd.resolve("verification").resolve("verification.jar")
        if (nextToApp.toFile().exists()) return nextToApp
        return cwd.resolve("verification.jar").takeIf { it.toFile().exists() }
    }

    fun run(verificationRoot: Path): List<VerificationResult> {
        val jarUrl: URL = verificationRoot.toUri().toURL()
        val parent = Thread.currentThread().contextClassLoader
        val loader = java.net.URLClassLoader.newInstance(arrayOf(jarUrl), parent)
        val previous = Thread.currentThread().contextClassLoader
        Thread.currentThread().contextClassLoader = loader
        try {
            val request: LauncherDiscoveryRequest = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectClasspathRoots(setOf(verificationRoot)))
                .build()
            val listener = AggregateListener()
            val config = LauncherConfig.builder()
                .enableTestExecutionListenerAutoRegistration(false)
                .addTestExecutionListeners(listener)
                .build()
            val launcher: Launcher = LauncherFactory.create(config)
            launcher.execute(request)
            return listener.getResults()
        } finally {
            Thread.currentThread().contextClassLoader = previous
        }
    }

    fun runDefault(): List<VerificationResult>? {
        val path = findVerificationJarPath() ?: return null
        return run(path)
    }
}
