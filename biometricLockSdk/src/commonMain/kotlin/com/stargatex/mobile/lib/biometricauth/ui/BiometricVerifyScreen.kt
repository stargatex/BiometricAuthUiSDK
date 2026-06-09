package com.stargatex.mobile.lib.biometricauth.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAuthResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAvailabilityResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricPromptConfig
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.LockConfig
import com.stargatex.mobile.lib.biometricauth.domain.biometric.repository.BiometricAuthRepository
import com.stargatex.mobile.lib.biometricauth.domain.biometric.usecase.AuthorizeBiometricUseCase
import com.stargatex.mobile.lib.biometricauth.domain.biometric.usecase.BiometricAuthUseCase
import com.stargatex.mobile.lib.biometricauth.domain.biometric.usecase.BiometricAvailabilityUseCase
import com.stargatex.mobile.lib.biometricauth.ui.model.config.BiometricUiTextConfig
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable screen that handles biometric verification.
 * It checks for biometric availability, displays appropriate messages,
 * and initiates the authentication process. It also handles various
 * outcomes of the authentication attempt.
 *
 * @param verifyViewModel The [BiometricVerifyViewModel] used to manage biometric state and actions.
 * @param shouldCheckAvailability A boolean indicating whether to check for biometric availability
 *                                 when the screen is launched. Defaults to `true`.
 * @param lockConfig The [LockConfig] to be used for biometric authentication.
 *                   Defaults to a [LockConfig] with the default [BiometricPromptConfig].
 * @param uiTextConfig The [BiometricUiTextConfig] to customize the text displayed on the screen.
 *                     Defaults to [BiometricUiTextConfig.default].
 * @param onAuthSuccess A lambda function to be invoked when biometric authentication is successful.
 * @param onNoEnrollment A lambda function to be invoked if no biometrics are enrolled on the device.
 * @param onFallback A lambda function to be invoked if biometric hardware is not available,
 *                   allowing for an alternative authentication method.
 * @param onAuthFailure A lambda function to be invoked when biometric authentication fails,
 *                      providing an error message string.
 *
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
@Composable
internal fun BiometricVerifyScreen(
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

    biometricVerifyEffects(
        verifyViewModel = verifyViewModel,
        shouldCheckAvailability = shouldCheckAvailability,
        lockConfig = lockConfig,
        availability = availability,
        authenticationResult = authenticationResult,
        authFailedMessage = uiTextConfig.authFailed,
        onAuthSuccess = onAuthSuccess,
        onNoEnrollment = onNoEnrollment,
        onFallback = onFallback,
        onAuthFailure = onAuthFailure
    )

    val statusText = when (val result = authenticationResult) {
        is BiometricAuthResult.Success -> uiTextConfig.authSuccess
        is BiometricAuthResult.Failed -> uiTextConfig.authFailed
        is BiometricAuthResult.Error -> "${uiTextConfig.errorPrefix}: ${result.message}"
        BiometricAuthResult.NegativeButtonClick -> uiTextConfig.authCancelled
        BiometricAuthResult.AttemptExhausted -> uiTextConfig.authExhausted
        null -> when (availability) {
            BiometricAvailabilityResult.Available -> uiTextConfig.available
            BiometricAvailabilityResult.NoEnrollment -> uiTextConfig.noEnrollment
            BiometricAvailabilityResult.HardwareUnavailable -> uiTextConfig.hardwareUnavailable
            BiometricAvailabilityResult.NoHardware -> uiTextConfig.noHardware
            BiometricAvailabilityResult.Unknown -> uiTextConfig.unknown
            null -> ""
        }
    }

    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = uiTextConfig.screenTitle,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = colors.onBackground
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = lockConfig.biometricPromptConfig.subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Spacer(Modifier.weight(1f))

        BiometricTargetIndicator(
            baseColor = colors.surfaceVariant,
            outerRingColor = colors.outlineVariant,
            middleRingColor = colors.primary.copy(alpha = 0.35f),
            innerRingColor = colors.primary.copy(alpha = 0.5f)
        )

        if (statusText.isNotBlank()) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }

        Spacer(Modifier.weight(1.2f))

        OutlinedButton(
            onClick = onFallback,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, colors.outline),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = colors.onSurface
            )
        ) {
            Text(
                text = uiTextConfig.fallbackButtonLabel,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
internal fun biometricVerifyEffects(
    verifyViewModel: BiometricVerifyViewModel,
    shouldCheckAvailability: Boolean,
    lockConfig: LockConfig,
    availability: BiometricAvailabilityResult?,
    authenticationResult: BiometricAuthResult?,
    authFailedMessage: String,
    onAuthSuccess: () -> Unit,
    onNoEnrollment: () -> Unit,
    onFallback: () -> Unit,
    onAuthFailure: (String) -> Unit
) {
    LaunchedEffect(shouldCheckAvailability) {
        if (shouldCheckAvailability) {
            verifyViewModel.checkBiometricAvailability()
        }
    }

    LaunchedEffect(availability) {
        when (availability) {
            BiometricAvailabilityResult.Available -> verifyViewModel.startAuthentication(lockConfig)
            BiometricAvailabilityResult.NoEnrollment -> onNoEnrollment()
            BiometricAvailabilityResult.NoHardware -> onFallback()
            BiometricAvailabilityResult.HardwareUnavailable,
            BiometricAvailabilityResult.Unknown,
            null -> Unit
        }
    }

    LaunchedEffect(authenticationResult) {
        when (val result = authenticationResult) {
            is BiometricAuthResult.Success -> onAuthSuccess()
            is BiometricAuthResult.Failed -> onAuthFailure(authFailedMessage)
            is BiometricAuthResult.Error -> onAuthFailure(result.message)
            BiometricAuthResult.NegativeButtonClick,
            BiometricAuthResult.AttemptExhausted,
            null -> Unit
        }
    }
}

@Composable
private fun BiometricTargetIndicator(
    modifier: Modifier = Modifier,
    baseColor: Color,
    outerRingColor: Color,
    middleRingColor: Color,
    innerRingColor: Color
) {
    Box(
        modifier = modifier.size(112.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(baseColor, CircleShape)
                .border(1.dp, outerRingColor, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(90.dp)
                .border(2.dp, middleRingColor, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(66.dp)
                .border(2.dp, innerRingColor, CircleShape)
        )
    }

}

@Preview
@Composable
@Suppress("FunctionName")
private fun BiometricVerifyScreenPreview() {
    val previewRepository = object : BiometricAuthRepository {
        override suspend fun authenticate(lockConfig: LockConfig): BiometricAuthResult {
            return BiometricAuthResult.Success
        }

        override suspend fun canAuthenticate(): BiometricAvailabilityResult {
            return BiometricAvailabilityResult.Available
        }
    }

    val previewViewModel = BiometricVerifyViewModel(
        biometricAuthUseCase = BiometricAuthUseCase(
            authorizeBiometricUseCase = AuthorizeBiometricUseCase(previewRepository),
            biometricAvailabilityUseCase = BiometricAvailabilityUseCase(previewRepository)
        ),
        biometricAvailabilityUseCase = BiometricAvailabilityUseCase(previewRepository)
    )

    BiometricVerifyScreen(
        verifyViewModel = previewViewModel,
        shouldCheckAvailability = true,
        lockConfig = LockConfig(BiometricPromptConfig.default()),
        uiTextConfig = BiometricUiTextConfig.default()
    )
}
