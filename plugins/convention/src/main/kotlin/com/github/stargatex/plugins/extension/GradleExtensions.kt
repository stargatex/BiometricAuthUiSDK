package com.github.stargatex.plugins.extension

import org.gradle.api.Project
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