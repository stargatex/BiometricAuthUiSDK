package com.stargatex.mobile.lib.biometricauth.domain.biometric.model

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
internal sealed class BiometricAuthResult {
    data object Success : BiometricAuthResult()
    data object Failed : BiometricAuthResult()
    data class Error(val message: String) : BiometricAuthResult()
    data object NegativeButtonClick : BiometricAuthResult()
    data object AttemptExhausted : BiometricAuthResult()
}
