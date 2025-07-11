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

public interface PINKeyXFacade {
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

    public fun clearStore(platformContextProvider: PlatformContextProvider)
}

public object PINKeyX : PINKeyXFacade {
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

    override fun clearStore(platformContextProvider: PlatformContextProvider) {
        clearPinStore(platformContextProvider)
    }
}


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

internal fun clearPinStore(platformContextProvider: PlatformContextProvider) {
    PinAuthLibDI.start(platformContextProvider)
    val clearPINUseCase: ClearPINUseCase = PinAuthLibDI.getKoin().get()
    clearPINUseCase.invoke()
}
