package com.stargatex.mobile.lib.biometricauth.di

import org.koin.core.Koin
import org.koin.core.KoinApplication

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
object BiometricAuthLibDI {
    private var koinApp: KoinApplication? = null


    fun start(platformContextProvider: PlatformContextProvider) {
        if (koinApp == null) {
            koinApp = KoinApplication.init()
                .modules(libMainModule(platformContextProvider))
        }
    }

    fun stop() {
        koinApp?.close()
        koinApp = null
    }

    fun getKoin(): Koin = koinApp!!.koin
}