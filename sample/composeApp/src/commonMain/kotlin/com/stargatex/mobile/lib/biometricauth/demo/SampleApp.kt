package com.stargatex.mobile.lib.biometricauth.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stargatex.mobile.lib.biometricauth.BioKeyX
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricPromptConfig
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.LockConfig
import com.stargatex.mobile.lib.biometricauth.ui.model.config.BiometricUiTextConfig
import com.stargatex.mobile.lib.pinauth.PINKeyX
import com.stargatex.mobile.lib.pinauth.domain.model.PinMode
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.annotation.KoinExperimentalAPI
import com.stargatex.mobile.lib.biometricauth.di.PlatformContextProvider as BioPlatformContextProvider
import com.stargatex.mobile.lib.pinauth.di.PlatformContextProvider as PinPlatformContextProvider

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun SampleApp(
    bioPlatformContextProvider: BioPlatformContextProvider,
    pinPlatformContextProvider: PinPlatformContextProvider,
    shouldCheckAvailability: Boolean = true,
    onAuthSuccess: () -> Unit = {},
    onNoEnrollment: () -> Unit = {},
    onFallback: () -> Unit = {},
    onAuthFailure: (String) -> Unit = {}
) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "biometric") {
        composable("biometric") {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Authentication Methods")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate("biometric-headless") }) {
                    Text("Biometric Only (Headless)")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { navController.navigate("biometric-overlay") }) {
                    Text("Biometric on PIN (Overlay)")
                }
            }
        }

        composable("biometric-headless") {
            DeveloperBiometricScreen(
                platformContextProvider = bioPlatformContextProvider,
                onAuthSuccess = onAuthSuccess,
                onNoEnrollment = onNoEnrollment,
                onAuthFailure = onAuthFailure,
                onFallback = {
                    navController.popBackStack()
                },
                shouldCheckAvailability = shouldCheckAvailability
            )
        }

        composable("biometric-overlay") {
            LayeredBiometricPinScreen(
                platformContextProvider = bioPlatformContextProvider,
                pinPlatformContextProvider = pinPlatformContextProvider,
                shouldCheckAvailability = shouldCheckAvailability,
                onAuthSuccess = {
                    navController.navigate("dashboard")
                },
                onFallback = onFallback,
                onAuthFailure = onAuthFailure,
                onGoBack = { navController.popBackStack() }
            )
        }

        composable("pin") {
            PINKeyX.Compose(
                mode = PinMode.UNLOCK,
                platformContextProvider = pinPlatformContextProvider,
                shouldCheckAvailability = shouldCheckAvailability,
                onAuthSuccess = {
                    navController.navigate("dashboard")
                },
                onFallback = onFallback,
                onAuthFailure = onAuthFailure,
                additionalOptions = {
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { navController.navigate("biometric") }) {
                        Text("Use biometric")
                    }
                    TextButton(onClick = onFallback) {
                        Text("Logout")
                    }
                }
            )
        }

        composable("dashboard") {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    PINKeyX.clearStore(pinPlatformContextProvider)
                    navController.popBackStack()
                }) {
                    Text(modifier = Modifier.wrapContentSize(), text = "Clear Pin")
                }
            }
        }


    }
}

@Composable
private fun DeveloperBiometricScreen(
    platformContextProvider: BioPlatformContextProvider,
    shouldCheckAvailability: Boolean,
    onAuthSuccess: () -> Unit,
    onNoEnrollment: () -> Unit,
    onFallback: () -> Unit,
    onAuthFailure: (String) -> Unit
) {
    var shouldAuthenticate by remember { mutableStateOf(false) }
    var authStatus by remember { mutableStateOf("Tap to authenticate") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Developer-owned biometric screen")
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = authStatus)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            authStatus = "Authenticating..."
            shouldAuthenticate = true
        }) {
            Text("Authenticate with Biometrics")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = {
            shouldAuthenticate = false
            onFallback()
        }) {
            Text("Use PIN Instead")
        }
    }

    if (shouldAuthenticate) {
        BioKeyX.Authenticate(
            platformContextProvider = platformContextProvider,
            shouldCheckAvailability = shouldCheckAvailability,
            lockConfig = LockConfig(BiometricPromptConfig.default()),
            uiTextConfig = BiometricUiTextConfig.default(),
            onAuthSuccess = {
                authStatus = "Authentication successful"
                shouldAuthenticate = false
                onAuthSuccess()
            },
            onNoEnrollment = {
                authStatus = "No biometrics enrolled"
                shouldAuthenticate = false
                onNoEnrollment()
            },
            onFallback = {
                authStatus = "Fallback selected"
                shouldAuthenticate = false
                onFallback()
            },
            onAuthFailure = { errorMessage ->
                authStatus = "Authentication failed: $errorMessage"
                shouldAuthenticate = false
                onAuthFailure(errorMessage)
            }
        )
    }
}

@Composable
private fun LayeredBiometricPinScreen(
    platformContextProvider: BioPlatformContextProvider,
    pinPlatformContextProvider: PinPlatformContextProvider,
    shouldCheckAvailability: Boolean,
    onAuthSuccess: () -> Unit,
    onFallback: () -> Unit,
    onAuthFailure: (String) -> Unit,
    onGoBack: () -> Unit
) {
    var shouldAuthenticate by remember { mutableStateOf(false) }

    // Auto-trigger biometric on first load
    LaunchedEffect(Unit) {
        shouldAuthenticate = true
    }

    Box(modifier = Modifier.fillMaxSize().navigationBarsPadding()) {
        PINKeyX.Compose(
            mode = PinMode.UNLOCK,
            platformContextProvider = pinPlatformContextProvider,
            shouldCheckAvailability = shouldCheckAvailability,
            onAuthSuccess = {
                onAuthSuccess()
            },
            onFallback = onFallback,
            onAuthFailure = onAuthFailure
        )

        if (shouldAuthenticate) {
            BioKeyX.Authenticate(
                platformContextProvider = platformContextProvider,
                shouldCheckAvailability = shouldCheckAvailability,
                lockConfig = LockConfig(BiometricPromptConfig.default()),
                uiTextConfig = BiometricUiTextConfig.default(),
                onAuthSuccess = {
                    shouldAuthenticate = false
                    onAuthSuccess()
                },
                onNoEnrollment = {
                    shouldAuthenticate = false
                },
                onFallback = {
                    shouldAuthenticate = false
                    onFallback()
                },
                onAuthFailure = { errorMessage ->
                    shouldAuthenticate = false
                    onAuthFailure(errorMessage)
                }
            )
        }
    }
}
