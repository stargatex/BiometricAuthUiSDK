package com.stargatex.mobile.lib.biometricauth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.stargatex.mobile.lib.biometricauth.di.BiometricAuthLibDI
import com.stargatex.mobile.lib.biometricauth.di.PlatformContextProvider
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricPromptConfig
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.LockConfig
import com.stargatex.mobile.lib.biometricauth.ui.BiometricVerifyScreen
import com.stargatex.mobile.lib.biometricauth.ui.BiometricVerifyViewModel
import com.stargatex.mobile.lib.biometricauth.ui.model.config.BiometricUiTextConfig
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.annotation.KoinExperimentalAPI

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

interface BioKeyXFacade {
    @Composable
    fun Compose(
        platformContextProvider: PlatformContextProvider,
        shouldCheckAvailability: Boolean = true,
        lockConfig: LockConfig = LockConfig(
            BiometricPromptConfig.default()
        ),
        uiTextConfig: BiometricUiTextConfig = BiometricUiTextConfig.default(),
        onAuthSuccess: () -> Unit = {},
        onNoEnrollment: () -> Unit = {},
        onFallback: () -> Unit = {},
        onAuthFailure: (String) -> Unit = {}
    )
}

object BioKeyX : BioKeyXFacade {
    @Composable
    override fun Compose(
        platformContextProvider: PlatformContextProvider,
        shouldCheckAvailability: Boolean,
        lockConfig: LockConfig,
        uiTextConfig: BiometricUiTextConfig,
        onAuthSuccess: () -> Unit,
        onNoEnrollment: () -> Unit,
        onFallback: () -> Unit,
        onAuthFailure: (String) -> Unit
    ) = App(
        platformContextProvider,
        shouldCheckAvailability,
        lockConfig,
        uiTextConfig,
        onAuthSuccess,
        onNoEnrollment,
        onFallback,
        onAuthFailure
    )
}


@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun App(
    platformContextProvider: PlatformContextProvider,
    shouldCheckAvailability: Boolean = true,
    lockConfig: LockConfig,
    uiTextConfig: BiometricUiTextConfig,
    onAuthSuccess: () -> Unit = {},
    onNoEnrollment: () -> Unit = {},
    onFallback: () -> Unit = {},
    onAuthFailure: (String) -> Unit = {}
) {
    BiometricAuthLibDI.start(platformContextProvider)

    Base(
        verifyViewModel = BiometricAuthLibDI.getKoin().get(),
        shouldCheckAvailability = shouldCheckAvailability,
        lockConfig = lockConfig,
        uiTextConfig = uiTextConfig,
        onAuthSuccess = onAuthSuccess,
        onNoEnrollment = onNoEnrollment,
        onFallback = onFallback,
        onAuthFailure = onAuthFailure
    )

    DisposableEffect(Unit) {
        onDispose {
            BiometricAuthLibDI.stop()
        }
    }
}


@Composable
private fun Base(
    verifyViewModel: BiometricVerifyViewModel,
    shouldCheckAvailability: Boolean = true,
    lockConfig: LockConfig,
    uiTextConfig: BiometricUiTextConfig,
    onAuthSuccess: () -> Unit = {},
    onNoEnrollment: () -> Unit = {},
    onFallback: () -> Unit = {},
    onAuthFailure: (String) -> Unit = {}
) {
    BiometricVerifyScreen(
        verifyViewModel = verifyViewModel,
        shouldCheckAvailability = shouldCheckAvailability,
        lockConfig = lockConfig,
        uiTextConfig = uiTextConfig,
        onAuthSuccess = onAuthSuccess,
        onNoEnrollment = onNoEnrollment,
        onFallback = onFallback,
        onAuthFailure = onAuthFailure
    )
}