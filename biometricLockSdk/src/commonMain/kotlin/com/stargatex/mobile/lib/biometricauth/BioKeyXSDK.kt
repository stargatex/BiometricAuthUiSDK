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

/**
 * Facade for the BioKeyX SDK, providing a Composable function to integrate biometric authentication.
 * This interface defines the entry point for using the BioKeyX SDK within a Jetpack Compose UI.
 */
public interface BioKeyXFacade {
    @Composable
    public fun Compose(
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

/**
 * `BioKeyX` is the main entry point for the Biometric Authentication Library.
 * It provides a Composable function to integrate biometric authentication into your app.
 *
 * This object implements the `BioKeyXFacade` interface, offering a standardized way
 * to interact with the biometric authentication functionality.
 *
 * The `Compose` function within this object is responsible for setting up and displaying
 * the biometric authentication UI, handling the authentication process, and providing
 * callbacks for various authentication outcomes.
 */
public object BioKeyX : BioKeyXFacade {
    /**
     * Composable function that provides the biometric authentication UI.
     *
     * This function is the main entry point for integrating biometric authentication into a Compose UI.
     * It handles the display of the biometric prompt and manages the authentication flow.
     *
     * @param platformContextProvider Provides platform-specific context, such as the Android `Context`.
     * @param shouldCheckAvailability A boolean flag indicating whether to check for biometric availability
     * before attempting authentication. Defaults to `true`.
     * @param lockConfig Configuration for the biometric lock, including settings for the biometric prompt.
     * Defaults to [LockConfig] with [BiometricPromptConfig.default].
     * @param uiTextConfig Configuration for the UI text elements displayed during the authentication process.
     * Defaults to [BiometricUiTextConfig.default].
     * @param onAuthSuccess Callback invoked when biometric authentication is successful.
     * @param onNoEnrollment Callback invoked if no biometric credentials are enrolled on the device.
     * @param onFallback Callback invoked if the user chooses to use a fallback authentication method (e.g., PIN, pattern).
     * @param onAuthFailure Callback invoked when biometric authentication fails. It provides an error message string.
     */
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
    ): Unit = App(
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


/**
 * Internal Composable function that sets up the Biometric Authentication library.
 * It initializes the dependency injection framework and renders the [Base] Composable
 * which contains the UI for biometric verification.
 * It also handles the disposal of resources when the Composable is removed from the composition.
 *
 * @param platformContextProvider Provides platform-specific context, essential for the library to function.
 * @param shouldCheckAvailability A boolean flag indicating whether to check for biometric availability before prompting. Defaults to `true`.
 * @param lockConfig Configuration for the biometric lock, including prompt details.
 * @param uiTextConfig Configuration for the UI text elements displayed during the biometric authentication process.
 * @param onAuthSuccess A lambda function to be executed when biometric authentication is successful.
 * @param onNoEnrollment A lambda function to be executed if no biometrics are enrolled on the device.
 * @param onFallback A lambda function to be executed if the user chooses to use a fallback authentication method.
 * @param onAuthFailure A lambda function to be executed when biometric authentication fails, providing an error message.
 */
@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
internal fun App(
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


/**
 * Composable function that serves as the base for the biometric authentication UI.
 * It wraps the [BiometricVerifyScreen] and provides it with the necessary dependencies and configurations.
 *
 * @param verifyViewModel The view model responsible for handling biometric verification logic.
 * @param shouldCheckAvailability A boolean flag indicating whether to check for biometric availability before attempting authentication. Defaults to `true`.
 * @param lockConfig The configuration for the biometric prompt, such as title, subtitle, and description.
 * @param uiTextConfig The configuration for UI text elements, such as button labels and messages.
 * @param onAuthSuccess A callback function to be invoked when biometric authentication is successful.
 * @param onNoEnrollment A callback function to be invoked when no biometric credentials are enrolled on the device.
 * @param onFallback A callback function to be invoked when the user opts for a fallback authentication method.
 * @param onAuthFailure A callback function to be invoked when biometric authentication fails, providing an error message.
 */
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