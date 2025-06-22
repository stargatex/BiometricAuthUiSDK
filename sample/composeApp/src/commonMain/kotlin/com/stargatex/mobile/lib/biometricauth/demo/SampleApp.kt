package com.stargatex.mobile.lib.biometricauth.demo

import androidx.compose.runtime.Composable
import com.stargatex.mobile.lib.biometricauth.BioKeyX
import com.stargatex.mobile.lib.biometricauth.di.PlatformContextProvider
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricPromptConfig
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.LockConfig
import com.stargatex.mobile.lib.biometricauth.ui.model.config.BiometricUiTextConfig
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
    BioKeyX.Compose(
        platformContextProvider = platformContextProvider,
        shouldCheckAvailability = shouldCheckAvailability,
        lockConfig = LockConfig(BiometricPromptConfig.default()),
        uiTextConfig = BiometricUiTextConfig.default(),
        onAuthSuccess = onAuthSuccess,
        onNoEnrollment = onNoEnrollment,
        onFallback = onFallback,
        onAuthFailure = onAuthFailure
    )
}
