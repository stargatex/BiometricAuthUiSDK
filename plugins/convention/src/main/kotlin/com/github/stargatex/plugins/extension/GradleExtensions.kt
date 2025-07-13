package com.github.stargatex.plugins.extension

import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import java.util.Properties

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
fun Project.localProperties(): Properties {
    val props = Properties()
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { props.load(it) }
    }
    return props
}

fun MavenPublication.getCustomArtifactId(project: Project): String = when (project.name) {
    "biometricLockSdk" -> if (name == "kotlinMultiplatform") "bimetriclock" else "bimetriclock-$name"
    "pinLockUISdk" -> if (name == "kotlinMultiplatform") "pinlock" else "pinlock-$name"
    else -> "biometricauth-$name"
}

fun getLibraryDisplayName(projectName: String): String {
    return when (projectName) {
        "biometricLockSdk" -> "Biometric Lock SDK"
        "pinLockUISdk" -> "PIN Lock UI SDK"
        else -> projectName
    }
}


fun getLibraryDescription(projectName: String): String {
    return when (projectName) {
        "biometricLockSdk" -> "Biometric authentication SDK for Kotlin Multiplatform"
        "pinLockUISdk" -> "Custom PIN lock UI SDK for Kotlin Multiplatform"
        else -> "Kotlin Multiplatform SDK"
    }
}