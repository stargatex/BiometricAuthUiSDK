package com.github.stargatex.plugins.convention

import com.github.stargatex.plugins.extension.localProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.Sign
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
class KotlinMultiplatformSDKConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {

            group = "io.github.stargatex.mobile.lib"
            version = "1.0.0"

            pluginManager.apply("org.jetbrains.kotlin.multiplatform")
            pluginManager.apply("com.android.library")
            pluginManager.apply("maven-publish")
            pluginManager.apply("signing")
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

                val publishing = extensions.getByType<PublishingExtension>()
                publishing.repositories.maven {
                    val isSnapshot = version.toString().endsWith("SNAPSHOT")
                    url = uri(
                        if (isSnapshot)
                            "https://s01.oss.sonatype.org/content/repositories/snapshots"
                        else
                            "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2"
                    )
                    credentials {
                        username = localProps.getProperty("sonatypeUsername")
                        password = localProps.getProperty("sonatypePassword")
                    }
                }

                /*val javadocJar = tasks.register<Jar>("javadocJar") {
                    dependsOn("dokkaHtml")
                    archiveClassifier.set("javadoc")
                    from(layout.buildDirectory.dir("dokka/html"))
                }*/

                publishing.publications.withType<MavenPublication>().configureEach {
                    //artifact(javadocJar.get())

                    artifactId = when (project.name) {
                        "biometricLockSdk" -> if (name == "kotlinMultiplatform") "bimetriclock" else "bimetriclock-$name"
                        "pinLockUISdk" -> if (name == "kotlinMultiplatform") "pinlock" else "pinlock-$name"
                        else -> "biometricauth-$name"
                    }

                    println("📦 [${project.name}] publication '$name' → artifactId = '$artifactId'")

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

                val gpgKey = localProps.getProperty("gpgKeySecret")
                val gpgPass = localProps.getProperty("gpgKeyPassword")

                if (!gpgKey.isNullOrBlank() && !gpgPass.isNullOrBlank()) {
                    extensions.configure<SigningExtension> {
                        useInMemoryPgpKeys(gpgKey, gpgPass)
                        sign(publishing.publications)
                    }

                    tasks.withType<AbstractPublishToMaven>().configureEach {
                        dependsOn(tasks.withType<Sign>())
                    }
                }
            }
        }
    }
}