package com.stargatex.mobile.lib.biometricauth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAuthResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAvailabilityResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricPromptConfig
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.LockConfig
import com.stargatex.mobile.lib.biometricauth.ui.model.config.BiometricUiTextConfig

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

@Composable
fun BiometricVerifyScreen(
    verifyViewModel: BiometricVerifyViewModel,
    shouldCheckAvailability: Boolean = true,
    lockConfig: LockConfig = LockConfig(BiometricPromptConfig.default()),
    uiTextConfig: BiometricUiTextConfig = BiometricUiTextConfig.default(),
    onAuthSuccess: () -> Unit = {},
    onNoEnrollment: () -> Unit = {},
    onFallback: () -> Unit = {},
    onAuthFailure: (String) -> Unit = {}
) {
    val availability by verifyViewModel.availability.collectAsState()
    val authenticationResult by verifyViewModel.authResult.collectAsState()
    LaunchedEffect(shouldCheckAvailability) {
        if (shouldCheckAvailability) {
            verifyViewModel.checkBiometricAvailability()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(uiTextConfig.screenTitle, style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        when (availability) {
            null -> CircularProgressIndicator()
            BiometricAvailabilityResult.Available -> {
                Text(uiTextConfig.available)
                LaunchedEffect(availability) {
                    verifyViewModel.startAuthentication(lockConfig)
                }
            }

            BiometricAvailabilityResult.NoEnrollment -> {
                Text(uiTextConfig.noEnrollment)
                LaunchedEffect(availability) { onNoEnrollment() }
            }

            BiometricAvailabilityResult.HardwareUnavailable -> {
                Text(uiTextConfig.hardwareUnavailable)
            }

            BiometricAvailabilityResult.NoHardware -> {
                Text(uiTextConfig.noHardware)
                LaunchedEffect(availability) { onFallback() }
            }

            BiometricAvailabilityResult.Unknown -> Text(uiTextConfig.unknown)
        }

        Spacer(Modifier.height(24.dp))

        when (authenticationResult) {
            is BiometricAuthResult.Success -> {
                Text(uiTextConfig.authSuccess)
                LaunchedEffect(authenticationResult) { onAuthSuccess() }
            }

            is BiometricAuthResult.Failed -> {
                Text(uiTextConfig.authFailed)
                LaunchedEffect(authenticationResult) { onAuthFailure(uiTextConfig.authFailed) }
            }

            is BiometricAuthResult.Error -> {
                val error = (authenticationResult as BiometricAuthResult.Error).message
                Text("${uiTextConfig.errorPrefix}: $error")
                LaunchedEffect(error) { onAuthFailure(error) }
            }

            BiometricAuthResult.NegativeButtonClick -> Text(uiTextConfig.authCancelled)
            BiometricAuthResult.AttemptExhausted -> Text(uiTextConfig.authExhausted)
            null -> {}
        }
    }

}