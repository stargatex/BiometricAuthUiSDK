package com.stargatex.mobile.lib.pinauth.di

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.SynchronizedObject
import kotlinx.coroutines.internal.synchronized
import org.koin.core.Koin
import org.koin.core.KoinApplication
import kotlin.concurrent.Volatile

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
@OptIn(InternalCoroutinesApi::class)
internal object PinAuthLibDI {
    @Volatile
    private var koinApp: KoinApplication? = null

    private val lock = SynchronizedObject()

    fun start(platformContextProvider: PlatformContextProvider) {
        if (koinApp == null) {
            synchronized(lock) {
                if (koinApp == null) {
                    koinApp = KoinApplication.init()
                        .modules(libMainModule(platformContextProvider))
                }
            }
        }
    }

    fun stop() {
        synchronized(lock) {
            koinApp?.close()
            koinApp = null
        }
    }

    fun getKoin(): Koin = koinApp?.koin
        ?: error("PinAuthLibDI is not initialized. Call start() before getKoin().")
}