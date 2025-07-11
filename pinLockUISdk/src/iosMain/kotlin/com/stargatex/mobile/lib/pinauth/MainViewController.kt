package com.stargatex.mobile.lib.pinauth

import androidx.compose.ui.window.ComposeUIViewController
import com.stargatex.mobile.lib.pinauth.di.IOSPlatformContextProvider
import platform.UIKit.UIViewController

public fun MainViewController(): UIViewController = ComposeUIViewController {
    PINKeyX.Compose(
        IOSPlatformContextProvider()
    )
}