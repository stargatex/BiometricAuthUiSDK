package com.stargatex.mobile.lib.biometricauth

import androidx.compose.ui.window.ComposeUIViewController
import com.stargatex.mobile.lib.biometricauth.di.IOSPlatformContextProvider

fun MainViewController() = ComposeUIViewController {
    BioKeyX.Compose(
        IOSPlatformContextProvider()
    )
}