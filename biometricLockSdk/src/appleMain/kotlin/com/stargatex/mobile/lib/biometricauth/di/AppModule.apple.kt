package com.stargatex.mobile.lib.biometricauth.di

import platform.UIKit.UIViewController

internal actual typealias PlatformContext = UIViewController

public class IOSPlatformContextProvider() : PlatformContextProvider {
    override fun getPlatformContext(): PlatformContext? = null
}