package com.stargatex.mobile.lib.biometricauth.domain.biometric.usecase

import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAuthResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAvailabilityResult

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
class BiometricAuthUseCase(
    private val authorizeBiometricUseCase: AuthorizeBiometricUseCase,
    private val biometricAvailabilityUseCase: BiometricAvailabilityUseCase
) {
    suspend operator fun invoke(): BiometricAuthResult {
        val availability = biometricAvailabilityUseCase()
        if (availability != BiometricAvailabilityResult.Available) {
            return BiometricAuthResult.Error("Biometric not available: $availability")
        }
        return authorizeBiometricUseCase()
    }
}