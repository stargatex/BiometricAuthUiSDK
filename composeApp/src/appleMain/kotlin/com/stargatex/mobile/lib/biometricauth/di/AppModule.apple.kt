package com.stargatex.mobile.lib.biometricauth.di

import platform.UIKit.UIViewController

actual typealias PlatformContext = UIViewController

class IOSPlatformContextProvider() : PlatformContextProvider {
    override fun getPlatformContext(): PlatformContext? = null
}