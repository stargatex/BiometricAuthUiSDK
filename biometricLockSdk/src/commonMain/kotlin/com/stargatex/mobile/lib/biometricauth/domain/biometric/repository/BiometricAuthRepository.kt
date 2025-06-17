package com.stargatex.mobile.lib.biometricauth.domain.biometric.repository

import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAuthResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAvailabilityResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.LockConfig

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
interface BiometricAuthRepository {
    suspend fun authenticate(lockConfig : LockConfig): BiometricAuthResult
    suspend fun canAuthenticate(): BiometricAvailabilityResult
}