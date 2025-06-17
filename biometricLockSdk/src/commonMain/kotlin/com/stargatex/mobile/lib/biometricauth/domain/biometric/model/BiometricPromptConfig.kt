package com.stargatex.mobile.lib.biometricauth.domain.biometric.model

import androidx.compose.runtime.Composable
import com.stargatex.mobile.lib.bimetriclock.resources.Res
import org.jetbrains.compose.resources.stringResource

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
data class BiometricPromptConfig(
    val title: String,
    val subtitle: String,
    val description: String,
){
    companion object {
        @Composable
        fun default(): BiometricPromptConfig = BiometricPromptConfig(
            title = stringResource(Res.string.biometric_title),
            subtitle = stringResource(Res.string.biometric_subtitle),
            description = stringResource(Res.string.biometric_description)
        )
    }
}
