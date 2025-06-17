package com.stargatex.mobile.lib.biometricauth.platform.biometric.source

import com.stargatex.mobile.lib.biometricauth.platform.biometric.model.BiometricAuthResultDto
import com.stargatex.mobile.lib.biometricauth.platform.biometric.model.BiometricAvailabilityResultDto
import com.stargatex.mobile.lib.biometricauth.platform.biometric.model.LockConfigDto

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
expect class BiometricManagerUtil {
    suspend fun authenticate(lockConfigDto : LockConfigDto) : BiometricAuthResultDto
    suspend fun canAuthenticate() : BiometricAvailabilityResultDto
}