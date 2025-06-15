package com.stargatex.mobile.lib.biometricauth.di

import platform.UIKit.UIViewController

actual typealias PlatformContext = UIViewController

class IOSPlatformContextProvider(private val viewController: PlatformContext) : PlatformContextProvider {
    override fun getPlatformContext(): PlatformContext? = viewController
}