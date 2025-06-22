package com.stargatex.mobile.lib.pinauth.platform.store.di

import com.stargatex.mobile.lib.pinauth.di.PlatformContextProvider
import com.stargatex.mobile.lib.pinauth.platform.store.pref.PrefStore
import com.stargatex.mobile.lib.pinauth.platform.store.pref.SecurePrefStore
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */

fun userManagerModule(platformContextProvider: PlatformContextProvider) = module {
    includes(platformPrefStoreModule(platformContextProvider), prefStoreModule())

}

fun prefStoreModule() = module {
    single<PrefStore> { SecurePrefStore(get()) }
}

expect class KMMContext

expect fun platformPrefStoreModule(platformContextProvider: PlatformContextProvider): Module