package com.stargatex.mobile.lib.pinauth.platform.store.di

import android.app.Application
import android.content.Context
import com.liftric.kvault.KVault
import org.koin.dsl.module

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */

actual typealias KMMContext = Application


actual fun platformPrefStoreModule() = module {
    single<KVault> { KVault(context = (get() as Context).applicationContext, "user_mgr") }
}
