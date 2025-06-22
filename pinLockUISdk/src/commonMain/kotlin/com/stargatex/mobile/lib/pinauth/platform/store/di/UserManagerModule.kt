package com.stargatex.mobile.lib.pinauth.platform.store.di

import com.stargatex.mobile.lib.pinauth.platform.store.pref.PrefStore
import com.stargatex.mobile.lib.pinauth.platform.store.pref.SecurePrefStore
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */

fun userManagerModule() = module {
    includes(platformPrefStoreModule(), prefStoreModule())

}

fun prefStoreModule() = module {
    single<PrefStore> { SecurePrefStore(get()) }
}

expect class KMMContext

expect fun platformPrefStoreModule(): Module