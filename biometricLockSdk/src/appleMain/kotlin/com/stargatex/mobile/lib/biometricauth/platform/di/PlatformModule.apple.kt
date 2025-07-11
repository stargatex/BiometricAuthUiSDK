package com.stargatex.mobile.lib.biometricauth.platform.di

import com.stargatex.mobile.lib.biometricauth.di.PlatformContextProvider
import com.stargatex.mobile.lib.biometricauth.platform.biometric.source.BiometricManagerUtil
import com.stargatex.mobile.lib.biometricauth.swift.BiometricAuthHelper
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module

@OptIn(ExperimentalForeignApi::class)
internal actual fun platformBiometricModule(platformContextProvider: PlatformContextProvider): Module =
    module {
        single<BiometricManagerUtil> { BiometricManagerUtil(BiometricAuthHelper()) }
    }