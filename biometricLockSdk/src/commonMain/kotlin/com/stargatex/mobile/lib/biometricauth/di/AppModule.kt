package com.stargatex.mobile.lib.biometricauth.di

import com.stargatex.mobile.lib.biometricauth.data.di.dataModule
import com.stargatex.mobile.lib.biometricauth.domain.di.domainModule
import com.stargatex.mobile.lib.biometricauth.platform.di.platformModule
import com.stargatex.mobile.lib.biometricauth.ui.di.uiModule
import org.koin.dsl.module

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

internal fun libMainModule(platformContextProvider: PlatformContextProvider) = module {
    includes(platformModule(platformContextProvider), dataModule(), domainModule(), uiModule())
}

public expect class PlatformContext

/**
 * Provides access to the platform-specific context.
 *
 * This interface is used to abstract away the platform-specific context,
 * allowing the library to be used in different environments (e.g., Android, iOS).
 */
public interface PlatformContextProvider {
    /**
     * Retrieves the platform-specific context.
     *
     * This function is expected to be implemented by the platform-specific module (e.g., Android, iOS)
     * to provide the necessary context for platform-dependent operations.
     *
     * @return The platform-specific [PlatformContext] if available, otherwise `null`.
     */
    public fun getPlatformContext(): PlatformContext?
}