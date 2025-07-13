package com.stargatex.mobile.lib.biometricauth.domain.biometric.usecase

import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAvailabilityResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.repository.BiometricAuthRepository

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
internal class BiometricAvailabilityUseCase(
    private val repository: BiometricAuthRepository
) {
    suspend operator fun invoke(): BiometricAvailabilityResult {
        return repository.canAuthenticate()
    }
}