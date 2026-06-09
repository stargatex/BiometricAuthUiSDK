package com.stargatex.mobile.lib.biometricauth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.stargatex.mobile.lib.biometricauth.di.BiometricAuthLibDI
import com.stargatex.mobile.lib.biometricauth.di.PlatformContextProvider
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricPromptConfig
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.LockConfig
import com.stargatex.mobile.lib.biometricauth.ui.biometricVerifyEffects
import com.stargatex.mobile.lib.biometricauth.ui.BiometricVerifyScreen
import com.stargatex.mobile.lib.biometricauth.ui.BiometricVerifyViewModel
import com.stargatex.mobile.lib.biometricauth.ui.model.config.BiometricUiTextConfig
import org.koin.core.annotation.KoinExperimentalAPI


/**
 * Facade for the BioKeyX SDK, providing a Composable function to integrate biometric authentication.
 * This interface defines the entry point for using the BioKeyX SDK within a Jetpack Compose UI.
 *
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
public interface BioKeyXFacade {
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

    /**
     * Composable function that executes biometric authentication without rendering SDK UI.
     *
     * Use this when you want to drive authentication from your own screen while still using
     * the SDK biometric flow and callbacks.
     */
    @Composable
    public fun Authenticate(
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

    @Composable
    override fun Authenticate(
        platformContextProvider: PlatformContextProvider,
        shouldCheckAvailability: Boolean,
        lockConfig: LockConfig,
        uiTextConfig: BiometricUiTextConfig,
        onAuthSuccess: () -> Unit,
        onNoEnrollment: () -> Unit,
        onFallback: () -> Unit,
        onAuthFailure: (String) -> Unit
    ): Unit = AppHeadless(
        platformContextProvider = platformContextProvider,
        shouldCheckAvailability = shouldCheckAvailability,
        lockConfig = lockConfig,
        uiTextConfig = uiTextConfig,
        onAuthSuccess = onAuthSuccess,
        onNoEnrollment = onNoEnrollment,
        onFallback = onFallback,
        onAuthFailure = onAuthFailure
    )
}



/**
 * Initializes the biometric DI scope and returns the [BiometricVerifyViewModel], scoped to
 * [platformContextProvider]. The DI container is started exactly once per unique context instance
 * (guarded by [remember]) and torn down via [DisposableEffect] when the composable leaves the tree.
 *
 * Extracting this into a dedicated function avoids duplicating the lifecycle boilerplate across
 * [App] and [AppHeadless].
 */
@OptIn(KoinExperimentalAPI::class)
@Composable
private fun rememberBiometricViewModel(
    platformContextProvider: PlatformContextProvider
): BiometricVerifyViewModel {
    val verifyViewModel: BiometricVerifyViewModel = remember(platformContextProvider) {
        BiometricAuthLibDI.start(platformContextProvider)
        BiometricAuthLibDI.getKoin().get()
    }

    // Keyed on platformContextProvider so the scope is torn down and recreated if context changes.
    DisposableEffect(platformContextProvider) {
        onDispose {
            BiometricAuthLibDI.stop()
        }
    }

    return verifyViewModel
}

/**
 * Internal Composable function that sets up the Biometric Authentication library.
 * It initializes the dependency injection framework and renders the [BiometricVerifyScreen].
 * Resources are disposed when the Composable is removed from the composition.
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
@Composable
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
    val verifyViewModel = rememberBiometricViewModel(platformContextProvider)

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

/**
 * Headless variant of [App] — runs the full biometric authentication flow without
 * rendering any SDK-owned UI. Use this when you want to drive authentication from your
 * own screen while still using the SDK biometric flow and callbacks.
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
@Composable
internal fun AppHeadless(
    platformContextProvider: PlatformContextProvider,
    shouldCheckAvailability: Boolean = true,
    lockConfig: LockConfig,
    uiTextConfig: BiometricUiTextConfig = BiometricUiTextConfig.default(),
    onAuthSuccess: () -> Unit = {},
    onNoEnrollment: () -> Unit = {},
    onFallback: () -> Unit = {},
    onAuthFailure: (String) -> Unit = {}
) {
    val verifyViewModel = rememberBiometricViewModel(platformContextProvider)

    val availability by verifyViewModel.availability.collectAsState()
    val authenticationResult by verifyViewModel.authResult.collectAsState()

    biometricVerifyEffects(
        verifyViewModel = verifyViewModel,
        shouldCheckAvailability = shouldCheckAvailability,
        lockConfig = lockConfig,
        availability = availability,
        authenticationResult = authenticationResult,
        authFailedMessage = uiTextConfig.authFailed,
        onAuthSuccess = onAuthSuccess,
        onNoEnrollment = onNoEnrollment,
        onFallback = onFallback,
        onAuthFailure = onAuthFailure
    )
}
