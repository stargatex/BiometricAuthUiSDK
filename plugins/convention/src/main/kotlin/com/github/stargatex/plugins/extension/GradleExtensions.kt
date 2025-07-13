package com.github.stargatex.plugins.extension

import com.github.stargatex.plugins.extension.KotlinMultiplatformSDKConventionExtension
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import java.util.Properties

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
internal fun Project.localProperties(): Properties {
    val props = Properties()
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { props.load(it) }
    }
    return props
}

internal fun MavenPublication.getCustomArtifactId(
    project: Project,
    extension: KotlinMultiplatformSDKConventionExtension
): String {
    val customMapping = extension.customArtifactIds[project.name]
    if (customMapping != null) {
        return if (name == "kotlinMultiplatform") customMapping else "$customMapping-$name"
    }

    return when (project.name) {
        "biometricLockSdk" -> if (name == "kotlinMultiplatform") "bimetriclock" else "bimetriclock-$name"
        "pinLockUISdk" -> if (name == "kotlinMultiplatform") "pinlock" else "pinlock-$name"
        else -> if (name == "kotlinMultiplatform") "biometricauth" else "biometricauth-$name"
    }
}

internal fun getLibraryDisplayName(projectName: String): String {
    return when (projectName) {
        "biometricLockSdk" -> "Biometric Lock SDK"
        "pinLockUISdk" -> "PIN Lock UI SDK"
        else -> projectName
    }
}


internal fun getLibraryDescription(projectName: String): String {
    return when (projectName) {
        "biometricLockSdk" -> "Biometric authentication SDK for Kotlin Multiplatform"
        "pinLockUISdk" -> "Custom PIN lock UI SDK for Kotlin Multiplatform"
        else -> "Kotlin Multiplatform SDK"
    }
}

internal fun shouldSkipProject(project: Project): Boolean {
    val excludedPaths = listOf(
        ":sample",
        ":samples",
        ":test",
        ":tests"
    )

    return excludedPaths.any { project.path.startsWith(it) }
}