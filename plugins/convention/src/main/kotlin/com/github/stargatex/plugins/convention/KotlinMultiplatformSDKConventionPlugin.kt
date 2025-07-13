package com.github.stargatex.plugins.convention

import com.github.stargatex.plugins.extension.getCustomArtifactId
import com.github.stargatex.plugins.extension.localProperties
import com.github.stargatex.plugins.extension.shouldSkipProject
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import com.github.stargatex.plugins.extension.KotlinMultiplatformSDKConventionExtension

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
class KotlinMultiplatformSDKConventionPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        if (shouldSkipProject(project)) {
            project.logger.warn("KotlinMultiplatformSDKConventionPlugin should not be applied to ${project.path}. Skipping.")
            return
        }

        fun Project.configureBinaryCompatibilityValidator(extension: KotlinMultiplatformSDKConventionExtension) {

            extensions.configure<kotlinx.validation.ApiValidationExtension> {

                validationDisabled = extension.enableApiValidation

                if (extension.nonPublicMarkers.isNotEmpty()) {
                    nonPublicMarkers.addAll(extension.nonPublicMarkers)
                }

                val excludedProjectPaths = listOf(
                    ":sample",
                    ":samples",
                    ":test",
                    ":tests"
                )

                ignoredProjects.addAll(
                    rootProject.subprojects
                        .filter { subproject ->
                            excludedProjectPaths.any { subproject.path.startsWith(it) }
                        }
                        .map { it.name }
                )

                if (extension.ignoredProjects.isNotEmpty()) {
                    ignoredProjects.addAll(extension.ignoredProjects)
                }
            }
        }


        with(project) {

            val extension = extensions.create<KotlinMultiplatformSDKConventionExtension>(
                "sdkConvention",
                KotlinMultiplatformSDKConventionExtension::class.java
            )

            pluginManager.apply("org.jetbrains.kotlin.multiplatform")
            pluginManager.apply("com.android.library")
            pluginManager.apply("com.vanniktech.maven.publish")
            pluginManager.apply("org.jetbrains.dokka")
            pluginManager.apply("org.jetbrains.kotlinx.binary-compatibility-validator")

            afterEvaluate {
                group = extension.groupId
                version = determineVersion(extension)

                configureKotlinMultiplatform(extension)

                configureBinaryCompatibilityValidator(extension)

                configurePublishing(extension)
            }
        }
    }

    private fun Project.determineVersion(extension: KotlinMultiplatformSDKConventionExtension): String {
        // Get release version from CI
        val releaseVersion = project.findProperty("releaseVersion") as? String
        if (!releaseVersion.isNullOrBlank()) {
            return releaseVersion
        }


        val isSnapshot = project.findProperty("isSnapshot") as? String == "true"
        if (isSnapshot) {
            return "${extension.baseVersion}-SNAPSHOT"
        }


        return extension.baseVersion
    }

    private fun Project.configureKotlinMultiplatform(extension: KotlinMultiplatformSDKConventionExtension) {
        extensions.configure<KotlinMultiplatformExtension> {
            if (extension.enableAndroidTarget) {
                androidTarget {
                    compilations.all {
                        kotlinOptions {
                            jvmTarget = extension.jvmTarget
                        }
                    }
                }
            }

            if (extension.enableIosTargets) {
                iosX64()
                iosArm64()
                iosSimulatorArm64()
            }

            withSourcesJar(publish = extension.publishSources)
        }
    }

    private fun Project.configurePublishing(extension: KotlinMultiplatformSDKConventionExtension) {
        val localProps = localProperties()

        extensions.configure<MavenPublishBaseExtension> {
            publishToMavenCentral()

            val gpgKey = localProps.getProperty("gpgKeySecret")
            val gpgPass = localProps.getProperty("gpgKeyPassword")

            if (!gpgKey.isNullOrBlank() && !gpgPass.isNullOrBlank()) {
                signAllPublications()
            }

            pom {
                name.set(project.name)
                description.set(extension.description)
                url.set(extension.gitUrl)

                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://opensource.org/licenses/Apache-2.0")
                    }
                }

                developers {
                    developer {
                        name.set(extension.developerName)
                        email.set(extension.developerEmail)
                    }
                }

                scm {
                    connection.set("scm:git:${extension.gitUrl}.git")
                    developerConnection.set("scm:git:ssh://git@${extension.gitUrl.removePrefix("https://")}.git")
                    url.set(extension.gitUrl)
                }
            }
        }

        extensions.configure<PublishingExtension> {
            publications.withType<MavenPublication>().configureEach {
                artifactId = getCustomArtifactId(project, extension)
                println("📦 [${project.name}] publication '$name' → artifactId = '$artifactId'")
            }
        }
    }
}