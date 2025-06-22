package com.stargatex.mobile.lib.pinauth

import androidx.compose.ui.window.ComposeUIViewController
import com.stargatex.mobile.lib.pinauth.di.IOSPlatformContextProvider
import com.stargatex.mobile.lib.pinauth.BioKeyX

fun MainViewController() = ComposeUIViewController {
    BioKeyX.Compose(
        IOSPlatformContextProvider()
    )
}