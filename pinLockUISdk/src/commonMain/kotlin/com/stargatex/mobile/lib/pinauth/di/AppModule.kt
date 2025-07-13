package com.stargatex.mobile.lib.pinauth.di


import com.stargatex.mobile.lib.pinauth.domain.di.domainModule
import com.stargatex.mobile.lib.pinauth.platform.store.di.userManagerModule
import com.stargatex.mobile.lib.pinauth.ui.di.uiModule
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

public fun libMainModule(platformContextProvider: PlatformContextProvider): Module = module {
    includes(userManagerModule(platformContextProvider),domainModule(), uiModule())
}

public expect class PlatformContext

public interface PlatformContextProvider {
    public fun getPlatformContext(): PlatformContext?
}