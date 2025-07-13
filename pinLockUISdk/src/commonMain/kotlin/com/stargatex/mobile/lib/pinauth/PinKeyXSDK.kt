package com.stargatex.mobile.lib.pinauth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.stargatex.mobile.lib.pinauth.di.PinAuthLibDI
import com.stargatex.mobile.lib.pinauth.di.PlatformContextProvider
import com.stargatex.mobile.lib.pinauth.domain.model.LockConfig
import com.stargatex.mobile.lib.pinauth.domain.model.PinPromptConfig
import com.stargatex.mobile.lib.pinauth.domain.usecase.ClearPINUseCase
import com.stargatex.mobile.lib.pinauth.ui.PINVerifyViewModel
import com.stargatex.mobile.lib.pinauth.ui.PinVerifyScreen
import com.stargatex.mobile.lib.pinauth.ui.model.config.PinUiTextConfig
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.annotation.KoinExperimentalAPI

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

/**
 * Facade for interacting with the PINKeyX library.
 * This interface provides methods for composing the PIN authentication UI and clearing the PIN store.
 */
public interface PINKeyXFacade {
    /**
     * Composes the PIN authentication UI.
     *
     * This function is the main entry point for rendering the PIN authentication screen.
     * It handles the setup of dependencies and displays the appropriate UI based on the
     * provided configuration and the current authentication state.
     *
     * @param platformContextProvider Provides platform-specific context, such as the Android `Context`.
     * @param shouldCheckAvailability If true, the UI will first check if PIN authentication is available
     *                                and configured on the device. Defaults to true.
     * @param lockConfig Configuration for the lock screen behavior, including PIN prompt settings.
     *                   Defaults to a `LockConfig` with default `PinPromptConfig`.
     * @param uiTextConfig Configuration for the text displayed in the PIN UI.
     *                     Defaults to `PinUiTextConfig.default()`.
     * @param onAuthSuccess Callback invoked when PIN authentication is successful. Defaults to an empty lambda.
     * @param onFallback Callback invoked when a fallback authentication method is triggered (e.g., biometric prompt).
     *                   Defaults to an empty lambda.
     * @param onAuthFailure Callback invoked when PIN authentication fails. It receives a `String` message
     *                      describing the failure. Defaults to an empty lambda.
     */
    @Composable
    public fun Compose(
        platformContextProvider: PlatformContextProvider,
        shouldCheckAvailability: Boolean = true,
        lockConfig: LockConfig = LockConfig(
            PinPromptConfig.default()
        ),
        uiTextConfig: PinUiTextConfig = PinUiTextConfig.default(),
        onAuthSuccess: () -> Unit = {},
        onFallback: () -> Unit = {},
        onAuthFailure: (String) -> Unit = {}
    )

    /**
     * Clears any stored PIN data from the device.
     *
     * This function should be called when the user wants to remove their PIN configuration
     * or when the application needs to reset the PIN authentication state.
     *
     * @param platformContextProvider Provides platform-specific context, such as the Android `Context`.
     */
    public fun clearStore(platformContextProvider: PlatformContextProvider)
}

/**
 * Main entry point for the PINKeyX library.
 * This object implements the [PINKeyXFacade] to provide a simple way to integrate PIN authentication.
 *
 * Use [Compose] to render the PIN authentication UI and [clearStore] to remove any stored PIN data.
 */
public object PINKeyX : PINKeyXFacade {
    /**
     * Composes the PIN authentication UI.
     *
     * This function is the main entry point for rendering the PIN authentication screen.
     * It handles the setup of dependencies and displays the appropriate UI based on the
     * provided configuration and the current authentication state.
     *
     * @param platformContextProvider Provides platform-specific context, such as the Android `Context`.
     * @param shouldCheckAvailability If true, the UI will first check if PIN authentication is available
     *                                and configured on the device.
     * @param lockConfig Configuration for the lock screen behavior, including PIN prompt settings.
     * @param uiTextConfig Configuration for the text displayed in the PIN UI.
     * @param onAuthSuccess Callback invoked when PIN authentication is successful.
     * @param onFallback Callback invoked when a fallback authentication method is triggered (e.g., biometric prompt).
     * @param onAuthFailure Callback invoked when PIN authentication fails. It receives a `String` message
     *                      describing the failure.
     */
    @Composable
    override fun Compose(
        platformContextProvider: PlatformContextProvider,
        shouldCheckAvailability: Boolean,
        lockConfig: LockConfig,
        uiTextConfig: PinUiTextConfig,
        onAuthSuccess: () -> Unit,
        onFallback: () -> Unit,
        onAuthFailure: (String) -> Unit
    ): Unit = App(
        platformContextProvider,
        shouldCheckAvailability,
        lockConfig,
        uiTextConfig,
        onAuthSuccess,
        onFallback,
        onAuthFailure
    )

    /**
     * Clears any stored PIN data from the device.
     *
     * This function should be called when the user wants to remove their PIN configuration
     * or when the application needs to reset the PIN authentication state.
     * It internally calls [clearPinStore] to perform the actual clearing operation.
     *
     * @param platformContextProvider Provides platform-specific context, such as the Android `Context`.
     */
    override fun clearStore(platformContextProvider: PlatformContextProvider) {
        clearPinStore(platformContextProvider)
    }
}


/**
 * Internal composable function that sets up the PIN authentication library and displays the base UI.
 *
 * This function initializes the dependency injection framework ([PinAuthLibDI]) and then
 * delegates to the [Base] composable to render the actual PIN verification screen.
 * It also handles the cleanup of the dependency injection framework when the composable is disposed.
 *
 *
 * @param platformContextProvider Provides platform-specific context, such as the Android `Context`.
 * @param shouldCheckAvailability If true, the UI will first check if PIN authentication is available
 *                                and configured on the device. Defaults to true.
 * @param lockConfig Configuration for the lock screen behavior, including PIN prompt settings.
 * @param uiTextConfig Configuration for the text displayed in the PIN UI.
 * @param onAuthSuccess Callback invoked when PIN authentication is successful. Defaults to an empty lambda.
 * @param onFallback Callback invoked when a fallback authentication method is triggered (e.g., biometric prompt).
 *                   Defaults to an empty lambda.
 * @param onAuthFailure Callback invoked when PIN authentication fails. It receives a `String` message
 *                      describing the failure. Defaults to an empty lambda.
 */
@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
internal fun App(
    platformContextProvider: PlatformContextProvider,
    shouldCheckAvailability: Boolean = true,
    lockConfig: LockConfig,
    uiTextConfig: PinUiTextConfig,
    onAuthSuccess: () -> Unit = {},
    onFallback: () -> Unit = {},
    onAuthFailure: (String) -> Unit = {}
) {
    PinAuthLibDI.start(platformContextProvider)

    Base(
        verifyViewModel = PinAuthLibDI.getKoin().get(),
        shouldCheckAvailability = shouldCheckAvailability,
        lockConfig = lockConfig,
        uiTextConfig = uiTextConfig,
        onAuthSuccess = onAuthSuccess,
        onFallback = onFallback,
        onAuthFailure = onAuthFailure
    )

    DisposableEffect(Unit) {
        onDispose {
            PinAuthLibDI.stop()
        }
    }
}


/**
 * Internal composable function that renders the core PIN verification UI.
 *
 * This function is responsible for displaying the `PinVerifyScreen` and passing down
 * the necessary view model, configurations, and callbacks. It's not intended for direct
 * external use.
 *
 * @param verifyViewModel The view model responsible for handling PIN verification logic.
 * @param shouldCheckAvailability If true, the UI will first check if PIN authentication is available
 *                                and configured on the device. Defaults to true.
 * @param lockConfig Configuration for the lock screen behavior, including PIN prompt settings.
 * @param uiTextConfig Configuration for the text displayed in the PIN UI.
 * @param onAuthSuccess Callback invoked when PIN authentication is successful. Defaults to an empty lambda.
 * @param onFallback Callback invoked when a fallback authentication method is triggered (e.g., biometric prompt).
 *                   Defaults to an empty lambda.
 * @param onAuthFailure Callback invoked when PIN authentication fails. It receives a `String` message
 *                      describing the failure. Defaults to an empty lambda.
 */
@Composable
private fun Base(
    verifyViewModel: PINVerifyViewModel,
    shouldCheckAvailability: Boolean = true,
    lockConfig: LockConfig,
    uiTextConfig: PinUiTextConfig,
    onAuthSuccess: () -> Unit = {},
    onFallback: () -> Unit = {},
    onAuthFailure: (String) -> Unit = {}
) {
    PinVerifyScreen(
        verifyViewModel = verifyViewModel,
        shouldCheckAvailability = shouldCheckAvailability,
        lockConfig = lockConfig,
        uiTextConfig = uiTextConfig,
        onUnlockSuccess = onAuthSuccess,
        onFallback = onFallback,
        onAuthFailure = onAuthFailure
    )
}

/**
 * Internal function to clear the stored PIN data.
 *
 * This function initializes the dependency injection framework if it hasn't been already,
 * retrieves the [ClearPINUseCase] instance, and then invokes it to perform the
 * PIN clearing operation.
 *
 * This function is intended for internal use within the library and is called by the
 * public `clearStore` method in the [PINKeyX] object.
 *
 * @param platformContextProvider Provides platform-specific context, such as the Android `Context`,
 *                                 required for initializing dependencies.
 */
internal fun clearPinStore(platformContextProvider: PlatformContextProvider) {
    PinAuthLibDI.start(platformContextProvider)
    val clearPINUseCase: ClearPINUseCase = PinAuthLibDI.getKoin().get()
    clearPINUseCase.invoke()
}
