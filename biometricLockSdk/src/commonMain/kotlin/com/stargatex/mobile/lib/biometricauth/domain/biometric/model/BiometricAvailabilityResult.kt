package com.stargatex.mobile.lib.biometricauth.domain.biometric.model

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
internal sealed class BiometricAvailabilityResult {
    data object NoEnrollment : BiometricAvailabilityResult()
    data object HardwareUnavailable : BiometricAvailabilityResult()
    data object NoHardware : BiometricAvailabilityResult()
    data object Unknown : BiometricAvailabilityResult()
    data object Available : BiometricAvailabilityResult()
}
