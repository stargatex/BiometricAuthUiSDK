package com.stargatex.mobile.lib.biometricauth.demo

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stargatex.mobile.lib.biometricauth.BioKeyX
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricPromptConfig
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.LockConfig
import com.stargatex.mobile.lib.biometricauth.ui.model.config.BiometricUiTextConfig
import com.stargatex.mobile.lib.pinauth.PINKeyX
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.annotation.KoinExperimentalAPI
import com.stargatex.mobile.lib.biometricauth.di.PlatformContextProvider as BioPlatformContextProvider
import com.stargatex.mobile.lib.pinauth.di.PlatformContextProvider as PinPlatformContextProvider

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun SampleApp(
    bioPlatformContextProvider: BioPlatformContextProvider,
    pinPlatformContextProvider: PinPlatformContextProvider,
    shouldCheckAvailability: Boolean = true,
    onAuthSuccess: () -> Unit = {},
    onNoEnrollment: () -> Unit = {},
    onFallback: () -> Unit = {},
    onAuthFailure: (String) -> Unit = {}
) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "biometric") {
        composable("biometric") {
            BioKeyX.Compose(
                platformContextProvider = bioPlatformContextProvider,
                shouldCheckAvailability = shouldCheckAvailability,
                lockConfig = LockConfig(BiometricPromptConfig.default()),
                uiTextConfig = BiometricUiTextConfig.default(),
                onAuthSuccess = onAuthSuccess,
                onNoEnrollment = onNoEnrollment,
                onFallback = {
                    navController.navigate("pin")
                },
                onAuthFailure = onAuthFailure
            )
        }

        composable("pin") {
            PINKeyX.Compose(
                platformContextProvider = pinPlatformContextProvider,
                shouldCheckAvailability = shouldCheckAvailability,
                onAuthSuccess = onAuthSuccess,
                onFallback = onFallback,
                onAuthFailure = onAuthFailure
            )
        }
    }
}
