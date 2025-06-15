package com.stargatex.mobile.lib.biometricauth.domain.biometric.usecase

import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAuthResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.repository.BiometricAuthRepository

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
class AuthorizeBiometricUseCase(
    private val repository: BiometricAuthRepository
) {
    suspend operator fun invoke(): BiometricAuthResult {
        return repository.authenticate()
    }
}