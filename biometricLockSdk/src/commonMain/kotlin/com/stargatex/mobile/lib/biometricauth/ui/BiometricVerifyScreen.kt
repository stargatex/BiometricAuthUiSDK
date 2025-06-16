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
import org.koin.compose.viewmodel.koinViewModel

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

@Composable
fun BiometricVerifyScreen(
    verifyViewModel: BiometricVerifyViewModel = koinViewModel(),
    shouldCheckAvailability: Boolean = true,
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
        Text("Biometric Authentication", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        when (availability) {
            null -> CircularProgressIndicator()
            BiometricAvailabilityResult.Available -> {
                Text("Biometric is available.")
                LaunchedEffect(availability) {
                    verifyViewModel.startAuthentication()
                }
            }

            BiometricAvailabilityResult.NoEnrollment -> {
                Text("No biometric enrolled.")
                LaunchedEffect(availability) {
                    onNoEnrollment()
                }
            }
            BiometricAvailabilityResult.HardwareUnavailable -> Text("Hardware unavailable.")
            BiometricAvailabilityResult.NoHardware -> {
                Text("No biometric hardware found.")
                LaunchedEffect(availability) {
                    onFallback()
                }
            }
            BiometricAvailabilityResult.Unknown -> Text("Biometric availability unknown.")
        }

        Spacer(Modifier.height(24.dp))

        when (authenticationResult) {
            is BiometricAuthResult.Success -> {
                Text("Authentication successful.")
                LaunchedEffect(authenticationResult) { onAuthSuccess() }
            }

            is BiometricAuthResult.Failed -> {
                Text("Authentication failed.")
                LaunchedEffect(authenticationResult) { onAuthFailure("Authentication failed.") }
            }

            is BiometricAuthResult.Error -> {
                val error = (authenticationResult as BiometricAuthResult.Error).message
                Text("Error: $error")
                LaunchedEffect(error) { onAuthFailure(error) }
            }

            BiometricAuthResult.NegativeButtonClick -> {
                Text("Authentication cancelled.")
            }

            BiometricAuthResult.AttemptExhausted -> {
                Text("Too many failed attempts.")
            }

            null -> CircularProgressIndicator()
        }
    }

}