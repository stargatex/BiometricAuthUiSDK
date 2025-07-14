package com.github.stargatex.plugins.convention

import com.github.stargatex.plugins.extension.SdkManagementExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer
import org.gradle.kotlin.dsl.create

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
class SdkManagementConventionPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        if (project != project.rootProject) {
            return
        }

        val extension = project.extensions.create<SdkManagementExtension>("sdkManagement")

        project.afterEvaluate {
            createSdkManagementTasks(project, extension)
        }
    }

    private fun createSdkManagementTasks(project: Project, extension: SdkManagementExtension) {
        val sdkModules = extension.sdkModules.get()
        val enableiOSTests = extension.enableiOSTests.get()
        val enableAndroidTests = extension.enableAndroidTests.get()
        val enableLint = extension.enableLint.get()
        val enableDokka = extension.enableDokka.get()
        val enableCoverage = extension.enableCoverage.get()

        with(project.tasks) {
            createTestTasks(project, sdkModules, enableiOSTests, enableAndroidTests)
            createBuildTasks(project, sdkModules)
            createVerificationTasks(project, sdkModules, enableLint)
            createPublishingTasks(project, sdkModules)
            createDocumentationTasks(project, sdkModules, enableDokka)
            createCoverageTasks(project, sdkModules, enableCoverage)
            createUtilityTasks(sdkModules)
            createCompositeTasks()
        }
    }

    private fun TaskContainer.createTestTasks(
        rootProject: Project,
        sdkModules: List<String>,
        enableiOSTests: Boolean,
        enableAndroidTests: Boolean
    ) {
        register("testAllLibraries") {
            description = "Run all tests for SDK libraries"
            group = "verification"

            val testTasks = mutableListOf<String>()


            sdkModules.forEach { module ->
                val moduleProject = rootProject.findProject(module)
                if (moduleProject != null) {

                    if (hasTask(moduleProject, "jvmTest")) {
                        testTasks.add("$module:jvmTest")
                    } else if (hasTask(moduleProject, "test")) {
                        testTasks.add("$module:test")
                    }

                    if (enableAndroidTests) {
                        if (hasTask(moduleProject, "testDebugUnitTest")) {
                            testTasks.add("$module:testDebugUnitTest")
                        }
                    }

                    if (enableiOSTests && isMacOS()) {
                        if (hasTask(moduleProject, "iosSimulatorArm64Test")) {
                            testTasks.add("$module:iosSimulatorArm64Test")
                        }
                        if (hasTask(moduleProject, "iosX64Test")) {
                            testTasks.add("$module:iosX64Test")
                        }
                    }
                }
            }

            if (testTasks.isNotEmpty()) {
                dependsOn(testTasks)
            }

            doLast {
                if (testTasks.isEmpty()) {
                    println("No test tasks found for SDK modules")
                } else {
                    println("Executed test tasks: ${testTasks.joinToString(", ")}")
                }
            }
        }
    }

    private fun TaskContainer.createBuildTasks(rootProject: Project, sdkModules: List<String>) {
        register("assembleAllLibraries") {
            description = "Assemble all SDK libraries"
            group = "build"

            val assembleTasks = sdkModules.mapNotNull { module ->
                val moduleProject = rootProject.findProject(module)
                if (moduleProject != null && hasTask(moduleProject, "assemble")) {
                    "$module:assemble"
                } else null
            }

            if (assembleTasks.isNotEmpty()) {
                dependsOn(assembleTasks)
            }
        }

        register("cleanAllLibraries") {
            description = "Clean all SDK libraries"
            group = "build"

            val cleanTasks = sdkModules.mapNotNull { module ->
                val moduleProject = rootProject.findProject(module)
                if (moduleProject != null && hasTask(moduleProject, "clean")) {
                    "$module:clean"
                } else null
            }

            if (cleanTasks.isNotEmpty()) {
                dependsOn(cleanTasks)
            }
        }
    }

    private fun TaskContainer.createVerificationTasks(rootProject: Project, sdkModules: List<String>, enableLint: Boolean) {
        register("apiCheckAllLibraries") {
            description = "Check API compatibility for all SDK libraries"
            group = "verification"

            val apiCheckTasks = sdkModules.mapNotNull { module ->
                val moduleProject = rootProject.findProject(module)
                if (moduleProject != null && hasTask(moduleProject, "apiCheck")) {
                    "$module:apiCheck"
                } else null
            }

            if (apiCheckTasks.isNotEmpty()) {
                dependsOn(apiCheckTasks)
            }
        }

        if (enableLint) {
            register("lintAllLibraries") {
                description = "Run lint checks for all SDK libraries"
                group = "verification"

                val lintTasks = sdkModules.mapNotNull { module ->
                    val moduleProject = rootProject.findProject(module)
                    if (moduleProject != null) {
                        when {
                            hasTask(moduleProject, "lintDebug") -> "$module:lintDebug"
                            hasTask(moduleProject, "lint") -> "$module:lint"
                            else -> null
                        }
                    } else null
                }

                if (lintTasks.isNotEmpty()) {
                    dependsOn(lintTasks)
                }
            }
        }
    }

    private fun TaskContainer.createPublishingTasks(rootProject: Project, sdkModules: List<String>) {
        register("publishAllLibraries") {
            description = "Publish all SDK libraries to Maven Central"
            group = "publishing"

            val publishTasks = sdkModules.mapNotNull { module ->
                val moduleProject = rootProject.findProject(module)
                if (moduleProject != null) {
                    when {
                        hasTask(moduleProject, "publishToMavenCentral") -> "$module:publishToMavenCentral --no-configuration-cache"
                        hasTask(moduleProject, "publish") -> "$module:publish"
                        else -> null
                    }
                } else null
            }

            if (publishTasks.isNotEmpty()) {
                dependsOn(publishTasks)
            }
        }
    }

    private fun TaskContainer.createDocumentationTasks(rootProject: Project, sdkModules: List<String>, enableDokka: Boolean) {
        if (enableDokka) {
            register("dokkaAllLibraries") {
                description = "Generate documentation for all SDK libraries"
                group = "documentation"

                val dokkaTasks = sdkModules.mapNotNull { module ->
                    val moduleProject = rootProject.findProject(module)
                    if (moduleProject != null && hasTask(moduleProject, "dokkaHtml")) {
                        "$module:dokkaHtml"
                    } else null
                }

                if (dokkaTasks.isNotEmpty()) {
                    dependsOn(dokkaTasks)
                }
            }
        }
    }

    private fun TaskContainer.createCoverageTasks(rootProject: Project, sdkModules: List<String>, enableCoverage: Boolean) {
        if (enableCoverage) {
            register("coverageAllLibraries") {
                description = "Generate code coverage reports for all SDK libraries"
                group = "verification"

                val coverageTasks = sdkModules.mapNotNull { module ->
                    val moduleProject = rootProject.findProject(module)
                    if (moduleProject != null) {
                        when {
                            hasTask(moduleProject, "koverHtmlReport") -> "$module:koverHtmlReport"
                            hasTask(moduleProject, "jacocoTestReport") -> "$module:jacocoTestReport"
                            else -> null
                        }
                    } else null
                }

                if (coverageTasks.isNotEmpty()) {
                    dependsOn(coverageTasks)
                }

                doLast {
                    println("Coverage reports generated for all SDK libraries")
                    println("Check build/reports/kover/html/index.html in each module")
                }
            }
        }
    }

    private fun TaskContainer.createUtilityTasks(sdkModules: List<String>) {
        register("listSdkModules") {
            description = "List all configured SDK modules"
            group = "help"

            doLast {
                println("Configured SDK modules:")
                sdkModules.forEach { println("  - $it") }
            }
        }
    }

    private fun TaskContainer.createCompositeTasks() {
        register("ciCheck") {
            description = "Run all CI checks for SDK libraries"
            group = "verification"

            val dependencies = mutableListOf<String>().apply {
                add("testAllLibraries")
                add("apiCheckAllLibraries")
                add("assembleAllLibraries")

                if (findByName("lintAllLibraries") != null) {
                    add("lintAllLibraries")
                }
            }

            dependsOn(dependencies)
        }

        register("prepareRelease") {
            description = "Prepare all SDK libraries for release"
            group = "publishing"

            val dependencies = mutableListOf<String>().apply {
                add("cleanAllLibraries")
                add("testAllLibraries")
                add("apiCheckAllLibraries")
                add("assembleAllLibraries")

                if (findByName("lintAllLibraries") != null) {
                    add("lintAllLibraries")
                }
                if (findByName("dokkaAllLibraries") != null) {
                    add("dokkaAllLibraries")
                }
                if (findByName("coverageAllLibraries") != null) {
                    add("coverageAllLibraries")
                }
            }

            dependsOn(dependencies)

            doLast {
                println("All SDK libraries are ready for release!")
            }
        }
    }

    private fun hasTask(project: Project, taskName: String): Boolean {
        return try {
            project.tasks.findByName(taskName) != null
        } catch (e: Exception) {
            false
        }
    }


    private fun isMacOS(): Boolean {
        return System.getProperty("os.name").contains("Mac", ignoreCase = true)
    }
}