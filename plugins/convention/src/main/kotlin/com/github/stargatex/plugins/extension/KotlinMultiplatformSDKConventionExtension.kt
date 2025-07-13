package com.github.stargatex.plugins.extension

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
open class KotlinMultiplatformSDKConventionExtension {
    var groupId: String = "io.github.stargatex.mobile.lib"
    var baseVersion: String = "1.0.0"
    var description: String = "Biometric Auth and Custom PIN Kotlin Multiplatform UI SDK"
    var gitUrl: String = "https://github.com/stargatex/BiometricAuthUiSDK"
    var developerName: String = "Lahiru J"
    var developerEmail: String = "lahirudevx@gmail.com"

    var enableAndroidTarget: Boolean = true
    var enableIosTargets: Boolean = true
    var jvmTarget: String = "17"

    var publishSources: Boolean = false

    var customArtifactIds: Map<String, String> = mapOf(
        "biometricLockSdk" to "bimetriclock",
        "pinLockUISdk" to "pinlock"
    )

    var defaultArtifactId: String = "biometricauth"

    var enableApiValidation: Boolean = false
    var nonPublicMarkers: Set<String> = setOf()
    var ignoredProjects: Set<String> = setOf()


    fun customArtifactId(projectName: String, artifactId: String) {
        customArtifactIds = customArtifactIds + (projectName to artifactId)
    }

    fun addNonPublicMarker(marker: String) {
        nonPublicMarkers = nonPublicMarkers + marker
    }

    fun addIgnoredProject(projectName: String) {
        ignoredProjects = ignoredProjects + projectName
    }
}