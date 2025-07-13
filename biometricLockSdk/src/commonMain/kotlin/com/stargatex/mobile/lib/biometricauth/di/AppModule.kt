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

public interface PlatformContextProvider {
    public fun getPlatformContext(): PlatformContext?
}