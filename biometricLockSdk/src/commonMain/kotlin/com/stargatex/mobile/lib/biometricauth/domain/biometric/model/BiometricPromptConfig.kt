package com.stargatex.mobile.lib.biometricauth.domain.biometric.model

import androidx.compose.runtime.Composable
import com.stargatex.mobile.lib.bimetriclock.resources.Res
import com.stargatex.mobile.lib.bimetriclock.resources.biometric_description
import com.stargatex.mobile.lib.bimetriclock.resources.biometric_subtitle
import com.stargatex.mobile.lib.bimetriclock.resources.biometric_title
import org.jetbrains.compose.resources.stringResource

/**
 * Configuration data for the biometric prompt.
 *
 * This data class holds the strings to be displayed in the biometric prompt dialog.
 *
 * @property title The title of the biometric prompt.
 * @property subtitle The subtitle of the biometric prompt.
 * @property description A description providing more details for the biometric prompt.
 *
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
public data class BiometricPromptConfig(
    val title: String,
    val subtitle: String,
    val description: String,
){
    public companion object {
        /**
         * Creates a default [BiometricPromptConfig] with localized strings for title, subtitle, and description.
         *
         * @return A [BiometricPromptConfig] instance with default values.
         */
        @Composable
        public fun default(): BiometricPromptConfig = BiometricPromptConfig(
            title = stringResource(Res.string.biometric_title),
            subtitle = stringResource(Res.string.biometric_subtitle),
            description = stringResource(Res.string.biometric_description)
        )
    }
}
