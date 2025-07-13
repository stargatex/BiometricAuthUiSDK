package com.stargatex.mobile.lib.biometricauth.data.biometric.model

import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAuthResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAvailabilityResult
import com.stargatex.mobile.lib.biometricauth.platform.biometric.model.BiometricAuthResultDto
import com.stargatex.mobile.lib.biometricauth.platform.biometric.model.BiometricAvailabilityResultDto

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

internal fun BiometricAuthResultDto.toDomain(): BiometricAuthResult {
    return when (this) {
        is BiometricAuthResultDto.AttemptExhausted -> BiometricAuthResult.AttemptExhausted
        is BiometricAuthResultDto.Error -> BiometricAuthResult.Error(message = this.message)
        is BiometricAuthResultDto.Failed -> BiometricAuthResult.Failed
        is BiometricAuthResultDto.NegativeButtonClick -> BiometricAuthResult.NegativeButtonClick
        is BiometricAuthResultDto.Success -> BiometricAuthResult.Success
    }
}

internal fun BiometricAvailabilityResultDto.toDomain(): BiometricAvailabilityResult{
    return when (this) {
        is BiometricAvailabilityResultDto.HardwareUnavailable -> BiometricAvailabilityResult.HardwareUnavailable
        is BiometricAvailabilityResultDto.NoEnrollment -> BiometricAvailabilityResult.NoEnrollment
        is BiometricAvailabilityResultDto.NoHardware -> BiometricAvailabilityResult.NoHardware
        is BiometricAvailabilityResultDto.Available -> BiometricAvailabilityResult.Available
        is BiometricAvailabilityResultDto.Unknown -> BiometricAvailabilityResult.Unknown
    }
}
