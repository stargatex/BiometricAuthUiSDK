package com.stargatex.mobile.lib.biometricauth.domain.biometric.repository

import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAuthResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAvailabilityResult

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
interface BiometricAuthRepository {
    suspend fun authenticate(): BiometricAuthResult
    suspend fun canAuthenticate(): BiometricAvailabilityResult
}