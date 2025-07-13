package com.stargatex.mobile.lib.biometricauth

import androidx.compose.ui.window.ComposeUIViewController
import com.stargatex.mobile.lib.biometricauth.di.IOSPlatformContextProvider
import platform.UIKit.UIViewController

public fun MainViewController(): UIViewController = ComposeUIViewController {
    BioKeyX.Compose(
        IOSPlatformContextProvider()
    )
}