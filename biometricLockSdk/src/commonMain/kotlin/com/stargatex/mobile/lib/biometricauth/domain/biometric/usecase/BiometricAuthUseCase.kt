package com.stargatex.mobile.lib.biometricauth.domain.biometric.usecase

import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAuthResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAvailabilityResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.LockConfig

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
internal class BiometricAuthUseCase(
    private val authorizeBiometricUseCase: AuthorizeBiometricUseCase,
    private val biometricAvailabilityUseCase: BiometricAvailabilityUseCase
) {
    suspend operator fun invoke(lockConfig: LockConfig): BiometricAuthResult {
        val availability = biometricAvailabilityUseCase()
        if (availability != BiometricAvailabilityResult.Available) {
            return BiometricAuthResult.Error("Biometric not available: $availability")
        }
        return authorizeBiometricUseCase(lockConfig)
    }
}