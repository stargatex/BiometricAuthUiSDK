package com.stargatex.mobile.lib.biometricauth.platform.biometric.source

import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.stargatex.mobile.lib.biometricauth.platform.biometric.model.BiometricAuthResultDto
import com.stargatex.mobile.lib.biometricauth.platform.biometric.model.BiometricAvailabilityResultDto
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


actual class BiometricManagerUtil(private val activity: AppCompatActivity) {
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val executor = ContextCompat.getMainExecutor(activity)


    actual suspend fun authenticate(): BiometricAuthResultDto {
        this.preparePrompt(
            title = "Biometric Authentication",
            subtitle = "Log in using your biometric credential",
            description = "Place your finger on the sensor or use face recognition"
        )
        return suspendCancellableCoroutine { continuation ->
            var biometricPrompt =
                BiometricPrompt(
                    activity,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            when (errorCode) {
                                BiometricPrompt.ERROR_LOCKOUT, BiometricPrompt.ERROR_LOCKOUT_PERMANENT ->
                                    continuation.resume(BiometricAuthResultDto.AttemptExhausted)

                                BiometricPrompt.ERROR_NEGATIVE_BUTTON ->
                                    continuation.resume(BiometricAuthResultDto.NegativeButtonClick)

                                else -> continuation.resume(BiometricAuthResultDto.Error(errString.toString()))
                            }
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            continuation.resume(BiometricAuthResultDto.Success)
                        }

                        override fun onAuthenticationFailed() {
                            // Optional: trigger retry visuals if needed
                            continuation.resume(BiometricAuthResultDto.Failed)
                        }
                    })

            promptInfo.let {
                biometricPrompt.authenticate(it)
            }

        }
    }

    actual suspend fun canAuthenticate(): BiometricAvailabilityResultDto {
        val authenticators = BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        return when (BiometricManager.from(activity).canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricAvailabilityResultDto.Available
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricAvailabilityResultDto.NoEnrollment
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricAvailabilityResultDto.HardwareUnavailable
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricAvailabilityResultDto.NoHardware
            else -> BiometricAvailabilityResultDto.Unknown
        }
    }

    fun preparePrompt(title: String, subtitle: String, description: String): BiometricManagerUtil {
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .build()
        return this
    }
}