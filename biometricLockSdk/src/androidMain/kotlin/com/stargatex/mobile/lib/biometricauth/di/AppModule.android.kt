package com.stargatex.mobile.lib.biometricauth.di

import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity

internal actual typealias PlatformContext = AppCompatActivity

/**
 * Android-specific implementation of [PlatformContextProvider].
 *
 * This class holds a reference to the current Android [AppCompatActivity]
 * to provide it as the [PlatformContext].
 *
 * @property activity The [AppCompatActivity] instance to be provided as the platform context.
 */
public class AndroidPlatformContextProvider(private val activity: PlatformContext) :
    PlatformContextProvider {
    /**
     * Retrieves the platform-specific context.
     *
     * On Android, this will be the current [AppCompatActivity].
     *
     * @return The platform-specific context.
     */
    override fun getPlatformContext(): PlatformContext = activity
}

/**
 * A fake implementation of [PlatformContextProvider] for testing purposes on Android.
 *
 * This class provides a mock [PlatformContext] (which is an [AppCompatActivity])
 * without requiring a real Android environment.
 */
@VisibleForTesting
public class FakeAndroidPlatformContextProvider :
    PlatformContextProvider {
    /**
     * Returns a fake platform context.
     *
     * This method is intended for testing purposes only and should not be used in production code.
     * It creates and returns a new instance of [AppCompatActivity] each time it is called.
     *
     * @return A new instance of [AppCompatActivity].
     */
    override fun getPlatformContext(): PlatformContext = AppCompatActivity()
}