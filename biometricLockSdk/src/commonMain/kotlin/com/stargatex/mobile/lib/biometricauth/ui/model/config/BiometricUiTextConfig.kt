package com.stargatex.mobile.lib.biometricauth.ui.model.config

import androidx.compose.runtime.Composable
import com.stargatex.mobile.lib.bimetriclock.resources.Res
import com.stargatex.mobile.lib.bimetriclock.resources.biometric_available
import com.stargatex.mobile.lib.bimetriclock.resources.biometric_cancelled
import com.stargatex.mobile.lib.bimetriclock.resources.biometric_error_prefix
import com.stargatex.mobile.lib.bimetriclock.resources.biometric_exhausted
import com.stargatex.mobile.lib.bimetriclock.resources.biometric_failed
import com.stargatex.mobile.lib.bimetriclock.resources.biometric_hw_unavailable
import com.stargatex.mobile.lib.bimetriclock.resources.biometric_no_enrollment
import com.stargatex.mobile.lib.bimetriclock.resources.biometric_no_hw
import com.stargatex.mobile.lib.bimetriclock.resources.biometric_screen_title
import com.stargatex.mobile.lib.bimetriclock.resources.biometric_success
import com.stargatex.mobile.lib.bimetriclock.resources.biometric_unknown
import org.jetbrains.compose.resources.stringResource

/**
 * Configuration class for UI text elements in biometric authentication.
 *
 * This data class holds the string resources for various states and messages
 * displayed to the user during the biometric authentication process.
 *
 * @property screenTitle The title displayed on the biometric authentication screen.
 * @property available Message indicating biometric authentication is available.
 * @property noEnrollment Message indicating no biometrics are enrolled on the device.
 * @property hardwareUnavailable Message indicating biometric hardware is currently unavailable.
 * @property noHardware Message indicating the device does not have biometric hardware.
 * @property unknown Message for an unknown biometric authentication state or error.
 * @property authSuccess Message displayed upon successful biometric authentication.
 * @property authFailed Message displayed upon failed biometric authentication.
 * @property authCancelled Message displayed if biometric authentication is cancelled by the user.
 * @property authExhausted Message displayed when biometric authentication attempts are exhausted.
 * @property errorPrefix Prefix used for displaying detailed error messages.
 *
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
public data class BiometricUiTextConfig(
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
    public companion object {
        /**
         * Creates a [BiometricUiTextConfig] with default string resources.
         *
         * This function is composable and should be called from within a composable context.
         * It retrieves localized strings for various biometric authentication states and messages.
         *
         * @return A [BiometricUiTextConfig] instance populated with default localized strings.
         */
        @Composable
        public fun default(): BiometricUiTextConfig = BiometricUiTextConfig(
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
