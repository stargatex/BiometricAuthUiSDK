package com.stargatex.mobile.lib.pinauth.di

import platform.UIKit.UIViewController

internal actual typealias PlatformContext = UIViewController

/**
 * Provides a context for the iOS platform.
 * This class is specific to iOS and will return null as the context is not needed for biometric authentication on this platform.
 */
public class IOSPlatformContextProvider() : PlatformContextProvider {
    /**
     * Provides the current UIViewController for iOS platform.
     *
     * This function is part of the PlatformContextProvider interface and is specific to the iOS implementation.
     * In this default iOS implementation, it returns `null` because a specific UIViewController
     * context is not inherently available globally without a more specific application setup.
     *
     * Consumers on iOS might need to provide a more specific implementation of `PlatformContextProvider`
     * that can access the currently active UIViewController if biometric authentication requires it.
     *
     * @return The current UIViewController if available, otherwise `null`.
     */
    override fun getPlatformContext(): PlatformContext? = null
}