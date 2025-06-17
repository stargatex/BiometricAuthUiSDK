package com.stargatex.mobile.lib.biometricauth.demo

import androidx.compose.ui.window.ComposeUIViewController
import com.stargatex.mobile.lib.biometricauth.di.IOSPlatformContextProvider

fun MainViewController() = ComposeUIViewController { SampleApp(IOSPlatformContextProvider()) }