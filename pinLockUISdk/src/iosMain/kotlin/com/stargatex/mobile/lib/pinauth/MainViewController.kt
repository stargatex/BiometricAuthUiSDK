package com.stargatex.mobile.lib.pinauth

import androidx.compose.ui.window.ComposeUIViewController
import com.stargatex.mobile.lib.pinauth.di.IOSPlatformContextProvider

fun MainViewController() = ComposeUIViewController {
    PINKeyX.Compose(
        IOSPlatformContextProvider()
    )
}