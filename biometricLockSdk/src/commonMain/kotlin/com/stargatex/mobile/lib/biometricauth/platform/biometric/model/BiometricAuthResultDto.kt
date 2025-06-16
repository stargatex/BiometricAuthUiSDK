package com.stargatex.mobile.lib.biometricauth.platform.biometric.model

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
sealed class BiometricAuthResultDto {
    data object Success : BiometricAuthResultDto()
    data object Failed : BiometricAuthResultDto()
    data class Error(val message: String) : BiometricAuthResultDto()
    data object NegativeButtonClick : BiometricAuthResultDto()
    data object AttemptExhausted : BiometricAuthResultDto()
}
