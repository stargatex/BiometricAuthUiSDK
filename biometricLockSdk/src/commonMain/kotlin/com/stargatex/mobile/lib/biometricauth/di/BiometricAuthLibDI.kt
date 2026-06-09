package com.stargatex.mobile.lib.biometricauth.di

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
internal object BiometricAuthLibDI {

    @Volatile
    private var koinApp: KoinApplication? = null

    // kotlinx.coroutines.internal.synchronized requires a SynchronizedObject lock, not 'this'
    @OptIn(InternalCoroutinesApi::class)
    private val lock = SynchronizedObject()

    @OptIn(InternalCoroutinesApi::class)
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
        koinApp?.close()
        koinApp = null
    }

    fun getKoin(): Koin = koinApp?.koin
        ?: error("BiometricAuthLibDI is not started. Call start() before getKoin().")
}