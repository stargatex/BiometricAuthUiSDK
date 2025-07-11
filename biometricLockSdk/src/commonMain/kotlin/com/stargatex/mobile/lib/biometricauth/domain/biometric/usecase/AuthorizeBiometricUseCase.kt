package com.stargatex.mobile.lib.biometricauth.domain.biometric.usecase

import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAuthResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.LockConfig
import com.stargatex.mobile.lib.biometricauth.domain.biometric.repository.BiometricAuthRepository

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
internal class AuthorizeBiometricUseCase(
    private val repository: BiometricAuthRepository
) {
    suspend operator fun invoke(lockConfig: LockConfig): BiometricAuthResult {
        return repository.authenticate(lockConfig)
    }
}