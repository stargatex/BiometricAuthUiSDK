package com.stargatex.mobile.lib.biometricauth.platform.di

import com.stargatex.mobile.lib.biometricauth.di.PlatformContextProvider
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

internal fun platformModule(platformContextProvider: PlatformContextProvider) = module {
    includes(platformBiometricModule(platformContextProvider))
}

internal expect fun platformBiometricModule(platformContextProvider: PlatformContextProvider) : Module