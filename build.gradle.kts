plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.swiftklib) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.vanniktech.maven) apply false
    alias(libs.plugins.binary.compatibility.validator) apply false
    alias(libs.plugins.stargatex.kotlin.multiplatform.build.management)

}

sdkManagement {

    modules(
        ":biometricLockSdk"
    )

    // autoDetectSdkModules(project)

    enableiOSTests.set(false)
    enableAndroidTests.set(false)
    enableLint.set(false)
    enableDokka.set(false)
    enableCoverage.set(false)
}
