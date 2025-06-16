package com.stargatex.mobile.lib.biometricauth.data.di

import com.stargatex.mobile.lib.biometricauth.data.biometric.repository.BiometricAuthRepositoryImpl
import com.stargatex.mobile.lib.biometricauth.domain.biometric.repository.BiometricAuthRepository
import org.koin.dsl.module

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

fun dataModule() = module {
    single<BiometricAuthRepository> { BiometricAuthRepositoryImpl(get()) }
}