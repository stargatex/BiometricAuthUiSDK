package com.github.stargatex.plugins.extension

import org.gradle.api.Project

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
abstract class SdkManagementExtension {
    abstract val sdkModules: org.gradle.api.provider.ListProperty<String>
    abstract val enableiOSTests: org.gradle.api.provider.Property<Boolean>
    abstract val enableAndroidTests: org.gradle.api.provider.Property<Boolean>
    abstract val enableLint: org.gradle.api.provider.Property<Boolean>
    abstract val enableDokka: org.gradle.api.provider.Property<Boolean>
    abstract val enableCoverage: org.gradle.api.provider.Property<Boolean>

    init {
        // Set default values
        enableiOSTests.convention(true)
        enableAndroidTests.convention(true)
        enableLint.convention(true)
        enableDokka.convention(true)
        enableCoverage.convention(false)
    }

    // Convenience methods for configuration
    fun module(name: String) {
        sdkModules.add(name)
    }

    fun modules(vararg names: String) {
        sdkModules.addAll(*names)
    }

    fun autoDetectSdkModules(project: Project) {
        val detectedModules = project.subprojects
            .filter { it.name.endsWith("Sdk") }
            .map { ":${it.name}" }

        sdkModules.addAll(detectedModules)
    }
}