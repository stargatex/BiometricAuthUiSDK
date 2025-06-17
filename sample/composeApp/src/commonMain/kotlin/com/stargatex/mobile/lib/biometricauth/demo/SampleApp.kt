package com.stargatex.mobile.lib.biometricauth.demo

import androidx.compose.runtime.Composable
import com.stargatex.mobile.lib.biometricauth.App
import com.stargatex.mobile.lib.biometricauth.di.PlatformContextProvider
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.annotation.KoinExperimentalAPI

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun SampleApp(
    platformContextProvider: PlatformContextProvider,
    shouldCheckAvailability: Boolean = true,
    onAuthSuccess: () -> Unit = {},
    onNoEnrollment: () -> Unit = {},
    onFallback: () -> Unit = {},
    onAuthFailure: (String) -> Unit = {}
) {
    App(
        platformContextProvider,
        shouldCheckAvailability,
        onAuthSuccess,
        onNoEnrollment,
        onFallback,
        onAuthFailure
    )
}
