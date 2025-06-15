package com.stargatex.mobile.lib.biometricauth.platform.di

import com.stargatex.mobile.lib.biometricauth.di.PlatformContextProvider
import com.stargatex.mobile.lib.biometricauth.platform.biometric.source.BiometricManagerUtil
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun platformBiometricModule(platformContextProvider: PlatformContextProvider): Module = module {
    singleOf(::BiometricManagerUtil)
}