rootProject.name = "BiometricAuth"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("plugins")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenLocal()
        mavenCentral()
    }
}

include(":sample:composeApp")
include(":biometricLockSdk")
include(":pinLockUISdk")

gradle.projectsEvaluated {
    // skip sample app from publishin
    project(":sample:composeApp").apply {
        plugins.withId("com.vanniktech.maven.publish") {
            extensions.findByType<PublishingExtension>()?.apply {
                publications.clear()
            }
        }
    }
}