package com.stargatex.mobile.lib.biometricauth.demo

import androidx.compose.ui.window.ComposeUIViewController
import com.stargatex.mobile.lib.biometricauth.di.IOSPlatformContextProvider as BioIOSPlatformContextProvider
import com.stargatex.mobile.lib.pinauth.di.IOSPlatformContextProvider as PinIOSPlatformContextProvider

fun MainViewController() = ComposeUIViewController { SampleApp(BioIOSPlatformContextProvider(), PinIOSPlatformContextProvider()) }