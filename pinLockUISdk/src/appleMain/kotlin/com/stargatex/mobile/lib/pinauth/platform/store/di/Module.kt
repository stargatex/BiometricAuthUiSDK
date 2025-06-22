package com.stargatex.mobile.lib.pinauth.platform.store.di

import com.liftric.kvault.KVault
import com.stargatex.mobile.lib.pinauth.di.PlatformContextProvider
import org.koin.dsl.module
import platform.darwin.NSObject

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */

actual typealias KMMContext = NSObject

actual fun platformPrefStoreModule(platformContextProvider: PlatformContextProvider) = module {
    single<KVault> { KVault() }
}