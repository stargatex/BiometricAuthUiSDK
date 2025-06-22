package com.stargatex.mobile.lib.pinauth.platform.store.di

import android.app.Application
import com.liftric.kvault.KVault
import com.stargatex.mobile.lib.pinauth.di.PlatformContextProvider
import org.koin.dsl.module

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */

actual typealias KMMContext = Application


actual fun platformPrefStoreModule(platformContextProvider: PlatformContextProvider) = module {
    single<KVault> {
        KVault(
            context = platformContextProvider.getPlatformContext()!!.applicationContext,
            "pin_mgr"
        )
    }
}
