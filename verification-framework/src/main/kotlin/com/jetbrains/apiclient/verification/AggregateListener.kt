package com.jetbrains.apiclient.verification

import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan

class AggregateListener : TestExecutionListener {

    private val modulePassed = mutableMapOf<String, Int>()
    private val moduleTotal = mutableMapOf<String, Int>()
    private val testResults = mutableMapOf<String, Boolean>()
    private var testPlan: TestPlan? = null

    override fun testPlanExecutionStarted(testPlan: TestPlan) {
        this.testPlan = testPlan
        modulePassed.clear()
        moduleTotal.clear()
        testResults.clear()
    }

    override fun executionStarted(testIdentifier: TestIdentifier) {
        if (!testIdentifier.isTest) return
        val module = moduleFrom(testIdentifier) ?: return
        moduleTotal[module] = (moduleTotal[module] ?: 0) + 1
    }

    override fun executionFinished(testIdentifier: TestIdentifier, result: TestExecutionResult) {
        if (!testIdentifier.isTest) return
        val passed = result.status == org.junit.platform.engine.TestExecutionResult.Status.SUCCESSFUL
        testResults[testIdentifier.uniqueId] = passed
        val module = moduleFrom(testIdentifier) ?: return
        if (passed) {
            modulePassed[module] = (modulePassed[module] ?: 0) + 1
        }
    }

    fun getResults(): List<VerificationResult> {
        val allModules = (modulePassed.keys + moduleTotal.keys).toSet()
        return allModules.sorted().map { module ->
            VerificationResult(
                moduleName = module,
                passed = modulePassed[module] ?: 0,
                total = moduleTotal[module] ?: 0
            )
        }
    }

    private fun moduleFrom(testIdentifier: TestIdentifier): String? {
        var current: TestIdentifier? = testIdentifier
        val plan = testPlan
        while (current != null && plan != null) {
            val module = parseModuleFromUniqueId(current.uniqueId)
            if (module != null) return module
            current = plan.getParent(current).orElse(null)
        }
        parseModuleFromUniqueId(testIdentifier.uniqueId)?.let { return it }
        return "tests"
    }

    private fun parseModuleFromUniqueId(uniqueId: String): String? {
        val classSegment = uniqueId.split("/").find { it.startsWith("[class:") } ?: return null
        val className = classSegment.removePrefix("[class:").removeSuffix("]")
        val pkg = className.substringBeforeLast('.')
        return if (pkg.isEmpty()) null else pkg.substringAfterLast('.')
    }
}
