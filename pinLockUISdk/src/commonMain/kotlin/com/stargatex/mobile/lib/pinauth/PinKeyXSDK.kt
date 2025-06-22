package com.stargatex.mobile.lib.pinauth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.stargatex.mobile.lib.pinauth.di.PinAuthLibDI
import com.stargatex.mobile.lib.pinauth.di.PlatformContextProvider
import com.stargatex.mobile.lib.pinauth.domain.model.LockConfig
import com.stargatex.mobile.lib.pinauth.domain.model.PinPromptConfig
import com.stargatex.mobile.lib.pinauth.ui.PINVerifyViewModel
import com.stargatex.mobile.lib.pinauth.ui.PinVerifyScreen
import com.stargatex.mobile.lib.pinauth.ui.model.config.PinUiTextConfig
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.annotation.KoinExperimentalAPI

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

interface PINKeyXFacade {
    @Composable
    fun Compose(
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
}

object PINKeyX : PINKeyXFacade {
    @Composable
    override fun Compose(
        platformContextProvider: PlatformContextProvider,
        shouldCheckAvailability: Boolean,
        lockConfig: LockConfig,
        uiTextConfig: PinUiTextConfig,
        onAuthSuccess: () -> Unit,
        onFallback: () -> Unit,
        onAuthFailure: (String) -> Unit
    ) = App(
        platformContextProvider,
        shouldCheckAvailability,
        lockConfig,
        uiTextConfig,
        onAuthSuccess,
        onFallback,
        onAuthFailure
    )
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