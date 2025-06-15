package com.stargatex.mobile.lib.biometricauth

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.stargatex.mobile.lib.biometricauth.di.PlatformContextProvider
import com.stargatex.mobile.lib.biometricauth.di.appModule
import com.stargatex.mobile.lib.biometricauth.ui.BiometricVerifyScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinMultiplatformApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun App(platformContextProvider: PlatformContextProvider) {
    KoinMultiplatformApplication(config = KoinConfiguration {
        modules(appModule(platformContextProvider))
    }) {
        MaterialTheme { Base() }
    }
}

@Composable
private fun Base(
) {
    BiometricVerifyScreen()
}