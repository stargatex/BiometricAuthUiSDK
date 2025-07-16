# BiometricAuthUiSDK

A Compose Multiplatform SDK that provides secure biometric authentication and custom
PIN code functionality for Android and iOS applications.

## Overview

BiometricAuthUiSDK offers two separate SDKs:

- **BiometricLockSdk** - Core SDK for handling biometric authentication logic (fingerprint, face ID,
  etc.) across different platforms
- **PinLockUISdk** - Core SDK for handling custom PIN code lock across different platforms

## Features

### Biometric Authentication

- Fingerprint authentication
- Face ID support
- Cross-platform compatibility (Android & iOS)
- Fallback options

## Installation

Add the following dependency to your `build.gradle.kts` file to add `BiometricLockSdk`:

```kotlin
commonMain.dependencies {
    implementation("io.github.stargatex.mobile.lib:biometriclock:1.0.0")
}
```


## Quick Start

### Biometric Authentication

```kotlin
//...
import com.stargatex.mobile.lib.biometricauth.di.PlatformContextProvider as BioPlatformContextProvider
//...

@Composable
fun SampleApp(
    bioPlatformContextProvider: BioPlatformContextProvider,
    shouldCheckAvailability: Boolean = true,
    onAuthSuccess: () -> Unit = {},
    onNoEnrollment: () -> Unit = {},
    onFallback: () -> Unit = {},
    onAuthFailure: (String) -> Unit = {}
) {
    BioKeyX.Compose(
        platformContextProvider = bioPlatformContextProvider,
        shouldCheckAvailability = shouldCheckAvailability,
        lockConfig = LockConfig(
            BiometricPromptConfig.default()
        ),
        uiTextConfig = BiometricUiTextConfig.default(),
        onAuthSuccess = onAuthSuccess,
        onNoEnrollment = onNoEnrollment,
        onFallback = onFallback,
        onAuthFailure = onAuthFailure
    )
}
```


## Configuration

### Biometric Settings

```kotlin
/**
 * @param lockConfig Configuration for the biometric lock, including settings for the biometric prompt.
 * Defaults to [LockConfig] with [BiometricPromptConfig.default] or you can customize as bellow.
 */
BiometricPromptConfig(
    title = "Biometric Authentication",
    subtitle = "Log in using your biometric credential",
    description = "Place your finger on the sensor or look at the camera"
)

/**
 * @param uiTextConfig Configuration for the UI text elements displayed during the authentication process.
 * Defaults to [BiometricUiTextConfig.default]  or you can customize as bellow.
 */

BiometricUiTextConfig(
    screenTitle = "Biometric Authentication",
    available = "Biometric authentication is available.",
    noEnrollment = "No biometric credentials enrolled. Please set up biometric authentication in your device settings.",
    hardwareUnavailable = "Biometric hardware is currently unavailable.",
    noHardware = "This device does not support biometric authentication.",
    unknown = "An unknown error occurred during biometric authentication.",
    authSuccess = "Authentication successful.",
    authFailed = "Authentication failed. Please try again.",
    authCancelled = "Authentication cancelled by user.",
    authExhausted = "Too many authentication attempts. Please try again later.",
    errorPrefix = "Error: "
)
```


## Changelog

See [CHANGELOG.md](CHANGELOG.md) for a detailed list of changes and version history.

---

Made with ❤️ by [StargateX Mobile](https://github.com/stargatex)
