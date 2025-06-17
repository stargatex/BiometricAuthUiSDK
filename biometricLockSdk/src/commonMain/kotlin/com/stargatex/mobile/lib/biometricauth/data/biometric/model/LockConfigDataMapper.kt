package com.stargatex.mobile.lib.biometricauth.data.biometric.model

import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricPromptConfig
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.LockConfig
import com.stargatex.mobile.lib.biometricauth.platform.biometric.model.BiometricPromptConfigDto
import com.stargatex.mobile.lib.biometricauth.platform.biometric.model.LockConfigDto

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */

fun LockConfig.toPlatform(): LockConfigDto {
    return LockConfigDto(biometricPromptConfig.toPlatform())
}

fun BiometricPromptConfig.toPlatform(): BiometricPromptConfigDto {
    return BiometricPromptConfigDto(
        title, subtitle, description
    )
}