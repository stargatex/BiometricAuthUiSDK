package com.stargatex.mobile.lib.pinauth.platform.store.di

import com.liftric.kvault.KVault
import org.koin.dsl.module
import platform.darwin.NSObject

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */

actual typealias KMMContext = NSObject

actual fun platformPrefStoreModule() = module {
    single<KVault> { KVault() }
}