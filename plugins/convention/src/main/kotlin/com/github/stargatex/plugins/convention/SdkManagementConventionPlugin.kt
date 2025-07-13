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
            createTestTasks(sdkModules, enableiOSTests, enableAndroidTests)
            createBuildTasks(sdkModules)
            createVerificationTasks(sdkModules, enableLint)
            createPublishingTasks(sdkModules)
            createDocumentationTasks(sdkModules, enableDokka)
            createCoverageTasks(sdkModules, enableCoverage)
            createUtilityTasks(sdkModules)
            createCompositeTasks()
        }
    }

    private fun TaskContainer.createTestTasks(
        sdkModules: List<String>,
        enableiOSTests: Boolean,
        enableAndroidTests: Boolean
    ) {
        register("testAllLibraries") {
            description = "Run all tests for SDK libraries"
            group = "verification"

            dependsOn(sdkModules.map { "$it:jvmTest" })

            if (enableAndroidTests) {
                dependsOn(sdkModules.map { "$it:testDebugUnitTest" })
            }

            if (enableiOSTests && isMacOS()) {
                dependsOn(sdkModules.flatMap { module ->
                    listOf(
                        "$module:iosSimulatorArm64Test",
                        "$module:iosX64Test"
                    )
                })
            }
        }
    }

    private fun TaskContainer.createBuildTasks(sdkModules: List<String>) {
        register("assembleAllLibraries") {
            description = "Assemble all SDK libraries"
            group = "build"
            dependsOn(sdkModules.map { "$it:assemble" })
        }

        register("cleanAllLibraries") {
            description = "Clean all SDK libraries"
            group = "build"
            dependsOn(sdkModules.map { "$it:clean" })
        }
    }

    private fun TaskContainer.createVerificationTasks(
        sdkModules: List<String>,
        enableLint: Boolean
    ) {
        register("apiCheckAllLibraries") {
            description = "Check API compatibility for all SDK libraries"
            group = "verification"
            dependsOn(sdkModules.map { "$it:apiCheck" })
        }

        if (enableLint) {
            register("lintAllLibraries") {
                description = "Run lint checks for all SDK libraries"
                group = "verification"
                dependsOn(sdkModules.map { "$it:lintDebug" })
            }
        }
    }

    private fun TaskContainer.createPublishingTasks(sdkModules: List<String>) {
        register("publishAllLibraries") {
            description = "Publish all SDK libraries to Maven Central"
            group = "publishing"
            dependsOn(sdkModules.map { "$it:publishToMavenCentral" })
        }
    }

    private fun TaskContainer.createDocumentationTasks(
        sdkModules: List<String>,
        enableDokka: Boolean
    ) {
        if (enableDokka) {
            register("dokkaAllLibraries") {
                description = "Generate documentation for all SDK libraries"
                group = "documentation"
                dependsOn(sdkModules.map { "$it:dokkaHtml" })
            }
        }
    }

    private fun TaskContainer.createCoverageTasks(
        sdkModules: List<String>,
        enableCoverage: Boolean
    ) {
        if (enableCoverage) {
            register("coverageAllLibraries") {
                description = "Generate code coverage reports for all SDK libraries"
                group = "verification"
                dependsOn(sdkModules.map { "$it:koverHtmlReport" })

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

    private fun isMacOS(): Boolean {
        return System.getProperty("os.name").contains("Mac", ignoreCase = true)
    }
}