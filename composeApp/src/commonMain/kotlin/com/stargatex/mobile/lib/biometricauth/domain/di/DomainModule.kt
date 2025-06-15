package com.stargatex.mobile.lib.biometricauth.domain.di

import com.stargatex.mobile.lib.biometricauth.domain.biometric.usecase.AuthorizeBiometricUseCase
import com.stargatex.mobile.lib.biometricauth.domain.biometric.usecase.BiometricAuthUseCase
import com.stargatex.mobile.lib.biometricauth.domain.biometric.usecase.BiometricAvailabilityUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

fun domainModule() = module {
    factoryOf(::AuthorizeBiometricUseCase)
    factoryOf(::BiometricAvailabilityUseCase)
    factoryOf(::BiometricAuthUseCase)
}