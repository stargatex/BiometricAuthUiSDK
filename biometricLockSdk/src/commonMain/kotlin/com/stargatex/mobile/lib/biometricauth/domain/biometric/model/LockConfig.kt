package com.stargatex.mobile.lib.biometricauth.domain.biometric.model

/**
 * Represents the configuration for the lock mechanism.
 *
 * This data class holds the necessary configuration for biometric authentication prompts.
 *
 * @property biometricPromptConfig The configuration for the biometric prompt.
 *
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
public data class LockConfig(
    val biometricPromptConfig: BiometricPromptConfig
)
