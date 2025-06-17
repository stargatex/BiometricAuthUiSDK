package com.stargatex.mobile.lib.biometricauth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.stargatex.mobile.lib.biometricauth.di.BiometricAuthLibDI
import com.stargatex.mobile.lib.biometricauth.di.PlatformContextProvider
import com.stargatex.mobile.lib.biometricauth.ui.BiometricVerifyScreen
import com.stargatex.mobile.lib.biometricauth.ui.BiometricVerifyViewModel
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
        onAuthSuccess: () -> Unit,
        onNoEnrollment: () -> Unit,
        onFallback: () -> Unit,
        onAuthFailure: (String) -> Unit
    ) = App(
        platformContextProvider,
        shouldCheckAvailability,
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
    onAuthSuccess: () -> Unit = {},
    onNoEnrollment: () -> Unit = {},
    onFallback: () -> Unit = {},
    onAuthFailure: (String) -> Unit = {}
) {
    BiometricAuthLibDI.start(platformContextProvider)

    Base(
        verifyViewModel = BiometricAuthLibDI.getKoin().get(),
        shouldCheckAvailability = shouldCheckAvailability,
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
    onAuthSuccess: () -> Unit = {},
    onNoEnrollment: () -> Unit = {},
    onFallback: () -> Unit = {},
    onAuthFailure: (String) -> Unit = {}
) {
    BiometricVerifyScreen(
        verifyViewModel = verifyViewModel,
        shouldCheckAvailability = shouldCheckAvailability,
        onAuthSuccess = onAuthSuccess,
        onNoEnrollment = onNoEnrollment,
        onFallback = onFallback,
        onAuthFailure = onAuthFailure
    )
}