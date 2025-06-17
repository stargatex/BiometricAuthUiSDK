package com.stargatex.mobile.lib.biometricauth.data.biometric.repository

import com.stargatex.mobile.lib.biometricauth.data.biometric.model.toDomain
import com.stargatex.mobile.lib.biometricauth.data.biometric.model.toPlatform
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAuthResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAvailabilityResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.LockConfig
import com.stargatex.mobile.lib.biometricauth.domain.biometric.repository.BiometricAuthRepository
import com.stargatex.mobile.lib.biometricauth.platform.biometric.source.BiometricManagerUtil

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
class BiometricAuthRepositoryImpl(private val biometricManagerUtil: BiometricManagerUtil) :
    BiometricAuthRepository {
    override suspend fun authenticate(lockConfig: LockConfig): BiometricAuthResult {
        return biometricManagerUtil.authenticate(lockConfig.toPlatform()).toDomain()
    }

    override suspend fun canAuthenticate(): BiometricAvailabilityResult {
        return biometricManagerUtil.canAuthenticate().toDomain()
    }

}