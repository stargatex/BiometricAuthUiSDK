package com.stargatex.mobile.lib.biometricauth.platform.biometric.source

import com.stargatex.mobile.lib.biometricauth.platform.biometric.model.BiometricAuthResultDto
import com.stargatex.mobile.lib.biometricauth.platform.biometric.model.BiometricAvailabilityResultDto

actual class BiometricManagerUtil {
    actual suspend fun authenticate(): BiometricAuthResultDto {
        return BiometricAuthResultDto.Success
    }

    actual suspend fun canAuthenticate(): BiometricAvailabilityResultDto {
        return BiometricAvailabilityResultDto.Available
    }
}