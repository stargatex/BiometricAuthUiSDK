package com.stargatex.mobile.lib.pinauth.di


import com.stargatex.mobile.lib.pinauth.domain.di.domainModule
import com.stargatex.mobile.lib.pinauth.ui.di.uiModule
import org.koin.dsl.module

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

fun libMainModule(platformContextProvider: PlatformContextProvider) = module {
    includes(domainModule(), uiModule())
}

expect class PlatformContext

interface PlatformContextProvider {
    fun getPlatformContext(): PlatformContext?
}