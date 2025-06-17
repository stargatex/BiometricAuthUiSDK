package com.stargatex.mobile.lib.biometricauth.ui.model.config

import androidx.compose.runtime.Composable
import com.stargatex.mobile.lib.bimetriclock.resources.Res
import org.jetbrains.compose.resources.stringResource

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
data class BiometricUiTextConfig(
    val screenTitle: String,
    val available: String,
    val noEnrollment: String,
    val hardwareUnavailable: String,
    val noHardware: String,
    val unknown: String,
    val authSuccess: String,
    val authFailed: String,
    val authCancelled: String,
    val authExhausted: String,
    val errorPrefix: String
) {
    companion object {
        @Composable
        fun default(): BiometricUiTextConfig = BiometricUiTextConfig(
            screenTitle = stringResource(Res.string.biometric_screen_title),
            available = stringResource(Res.string.biometric_available),
            noEnrollment = stringResource(Res.string.biometric_no_enrollment),
            hardwareUnavailable = stringResource(Res.string.biometric_hw_unavailable),
            noHardware = stringResource(Res.string.biometric_no_hw),
            unknown = stringResource(Res.string.biometric_unknown),
            authSuccess = stringResource(Res.string.biometric_success),
            authFailed = stringResource(Res.string.biometric_failed),
            authCancelled = stringResource(Res.string.biometric_cancelled),
            authExhausted = stringResource(Res.string.biometric_exhausted),
            errorPrefix = stringResource(Res.string.biometric_error_prefix)
        )
    }
}
