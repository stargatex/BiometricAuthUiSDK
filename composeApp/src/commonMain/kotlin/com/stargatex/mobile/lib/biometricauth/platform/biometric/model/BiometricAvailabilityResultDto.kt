package com.stargatex.mobile.lib.biometricauth.platform.biometric.model

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
sealed class BiometricAvailabilityResultDto {
    data object NoEnrollment : BiometricAvailabilityResultDto()
    data object HardwareUnavailable : BiometricAvailabilityResultDto()
    data object NoHardware : BiometricAvailabilityResultDto()
    data object Unknown : BiometricAvailabilityResultDto()
    data object Available : BiometricAvailabilityResultDto()
}
