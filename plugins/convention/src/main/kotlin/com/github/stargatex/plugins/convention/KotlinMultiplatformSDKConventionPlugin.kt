package com.github.stargatex.plugins.convention

import com.github.stargatex.plugins.extension.getCustomArtifactId
import com.github.stargatex.plugins.extension.localProperties
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
class KotlinMultiplatformSDKConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        if (project.path.startsWith(":sample")) {
            project.logger.warn("KotlinMultiplatformSDKConventionPlugin should not be applied to sample projects. Skipping ${project.path}")
            return
        }

        with(project) {

            group = "io.github.stargatex.mobile.lib"
            version = "1.0.0"

            pluginManager.apply("org.jetbrains.kotlin.multiplatform")
            pluginManager.apply("com.android.library")
            pluginManager.apply("com.vanniktech.maven.publish")
            pluginManager.apply("org.jetbrains.dokka")

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget()
                iosX64()
                iosArm64()
                iosSimulatorArm64()
                withSourcesJar(publish = false)
            }

            afterEvaluate {
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
                        description.set("Biometric Auth and Custom PIN Kotlin Multiplatform UI SDK")
                        url.set("https://github.com/stargatex/BiometricAuthUiSDK")

                        licenses {
                            license {
                                name.set("Apache-2.0")
                                url.set("https://opensource.org/licenses/Apache-2.0")
                            }
                        }

                        developers {
                            developer {
                                name.set("Lahiru J")
                                email.set("lahirudevx@gmail.com")
                            }
                        }

                        scm {
                            connection.set("scm:git:https://github.com/stargatex/BiometricAuthUiSDK.git")
                            developerConnection.set("scm:git:ssh://git@github.com:stargatex/BiometricAuthUiSDK.git")
                            url.set("https://github.com/stargatex/BiometricAuthUiSDK")
                        }
                    }
                }

                extensions.configure<PublishingExtension> {
                    publications.withType<MavenPublication>().configureEach {
                        artifactId = getCustomArtifactId(project)

                        println("📦 [${project.name}] publication '$name' → artifactId = '$artifactId'")
                    }
                }
            }
        }
    }
}