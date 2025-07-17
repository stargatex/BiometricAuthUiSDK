# BiometricAuthUiSDK

![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![iOS](https://img.shields.io/badge/iOS-000000?style=for-the-badge&logo=ios&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-8A2BE2?style=for-the-badge&logo=kotlin&logoColor=white)


A Compose Multiplatform SDK that provides biometric authentication and custom
PIN code functionality for Android and iOS applications.

## Overview

BiometricAuthUiSDK offers two separate SDKs:

- **BiometricLockSdk** ![Maven Central Version](https://img.shields.io/maven-central/v/io.github.stargatex.mobile.lib/pinlock)- Core SDK for handling biometric authentication logic (fingerprint, face ID,
  etc.) across different platforms
- **PinLockUISdk** ![Maven Central Version](https://img.shields.io/maven-central/v/io.github.stargatex.mobile.lib/biometriclock)- Core SDK for handling custom PIN code lock across different platforms

## BiometricLockSdk


### Biometric Authentication

- Fingerprint authentication
- Face ID support
- Cross-platform compatibility (Android & iOS)
- Fallback options

## Installation

Add the following dependency to your `build.gradle.kts` file to add `BiometricLockSdk` for Compose
Multiplatform Projects:

```kotlin
commonMain.dependencies {
  implementation("io.github.stargatex.mobile.lib:biometriclock:<version>")
}
```

Add the following dependency to your `build.gradle.kts` file to add `BiometricLockSdk` for Android
Projects:

```kotlin
dependencies {
  implementation("io.github.stargatex.mobile.lib:biometricLockSdk-android:<version>")
}
```


## Quick Start

### Biometric Authentication

The main entry point is the `BioKeyX.Compose(...)` function. You embed this in your UI where you want
to trigger biometric verification. It handles the display of the biometric prompt and provides
callbacks for various authentication outcomes.

```kotlin
@Composable
fun BioKeyX.Compose(
  platformContextProvider: PlatformContextProvider, // Provides platform-specific context
  shouldCheckAvailability: Boolean = true,          // Check for biometric availability first
  lockConfig: LockConfig = LockConfig(...),         // Customize prompt appearance & behavior
uiTextConfig: BiometricUiTextConfig = BiometricUiTextConfig(...), // Customize UI text
onAuthSuccess: () -> Unit,                        // Called on successful authentication
onNoEnrollment: () -> Unit,                       // Called if no biometrics are enrolled
onFallback: () -> Unit,                           // Called if user chooses fallback (e.g., PIN)
onAuthFailure: (String) -> Unit                   // Called on authentication failure
)
```

### Android App (Jetpack Compose)

In your Android application, `BioKeyX` requires an `AndroidPlatformContextProvider` which needs an `AppCompatActivity`.

```kotlin

@Composable
fun MyScreen(activity: AppCompatActivity) { // Pass the activity
  var showBiometricPrompt by remember { mutableStateOf(false) }
  var authStatus by remember { mutableStateOf("Awaiting authentication...") }

  // Create the platform context provider instance
  val androidPlatformContextProvider = remember { AndroidPlatformContextProvider(activity) }

  Column {
    Button(onClick = { showBiometricPrompt = true }) {
      Text("Unlock with Biometrics")
    }
    Text(text = authStatus)

    if (showBiometricPrompt) {
      BioKeyX.Compose(
        platformContextProvider = androidPlatformContextProvider,
        shouldCheckAvailability = true,
        lockConfig = LockConfig(BiometricPromptConfig.default()),
        uiTextConfig = BiometricUiTextConfig.default(),
        onAuthSuccess = {
          authStatus = "Authentication Successful!"
          showBiometricPrompt = false
          // Navigate to secure content or perform action
        },
        onNoEnrollment = {
          authStatus =
            "No biometrics enrolled. Please set up biometrics in device settings."
          showBiometricPrompt = false
          // BioKeyX might internally handle launching enrollment based on its config
        },
        onFallback = {
          authStatus = "User chose fallback (e.g., PIN/Pattern)."
          showBiometricPrompt = false
          // Handle fallback authentication
        },
        onAuthFailure = { errorMessage ->
          authStatus = "Authentication Failed: $errorMessage"
          showBiometricPrompt = false
        }
      )
    }
  }
}


//In your MainActivity.kt
class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      YourAppTheme {
        MySecureScreen(activity = this) // Pass the activity instance
      }
    }
  }
}

```


### Compose Multiplatform App (Shared UI)

For a Compose Multiplatform app, `BioKeyX.Compose` is called from your shared UI code (`commonMain`).
You'll rely on `expect/actual` implementations for `PlatformContextProvider` and `PlatformContext`.

```kotlin

// Expect declaration for getting the platform-specific provider
@Composable
expect fun rememberPlatformContextProvider(): PlatformContextProvider

@Composable
fun MyScreen() {
  var showBiometricPrompt by remember { mutableStateOf(false) }
  var authStatus by remember { mutableStateOf("Awaiting authentication...") }

  val platformContextProvider = rememberPlatformContextProvider()

  Column {
    Button(onClick = { showBiometricPrompt = true }) {
      Text("Unlock Feature")
    }
    Text(text = authStatus)

    if (showBiometricPrompt) {
      BioKeyX.Compose(
        platformContextProvider = platformContextProvider,
        shouldCheckAvailability = true,
        lockConfig = LockConfig(BiometricPromptConfig.default()),
        uiTextConfig = BiometricUiTextConfig.default(),
        onAuthSuccess = { /* ... */ },
        onNoEnrollment = { /* ... */ },
        onFallback = { /* ... */ },
        onAuthFailure = { /* ... */ }
      )
    }
  }
}

```
Android Specific (`androidMain/kotlin`) for `rememberPlatformContextProvider`:

```kotlin
@Composable
internal actual fun rememberPlatformContextProvider(): PlatformContextProvider {
  val activity = LocalContext.current as AppCompatActivity // Ensure your current context is an AppCompatActivity
  return remember { AndroidPlatformContextProvider(activity) }
}
```

iOS Specific (`iosMain/kotlin`) for `rememberPlatformContextProvider`:

```kotlin
@Composable
internal actual fun rememberPlatformContextProvider(): PlatformContextProvider {
  return remember { IOSPlatformContextProvider() }
}
```

### iOS App (via Compose Multiplatform)

When using Compose Multiplatform for iOS, the `BioKeyX.Compose` call in your shared UI (`commonMain`) remains the same.


### Additional Configuration

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


## PinLockUISdk

### Custom PIN Code

- PIN code interface
- Secure PIN storage and validation
- Cross-platform compatibility (Android & iOS)

## Installation

Add the following dependency to your `build.gradle.kts` file to add `PinLockUISdk` for Compose
Multiplatform Projects:

```kotlin
commonMain.dependencies {
  implementation("io.github.stargatex.mobile.lib:pinlock:<version>")
}
```

Add the following dependency to your `build.gradle.kts` file to add `PinLockUISdk` for Android
Projects:

```kotlin
dependencies {
  implementation("io.github.stargatex.mobile.lib:pinLockUISdk-android:<version>")
}
```

## Quick Start

### PIN Code Lock

**Displaying PIN UI**: The `PINKeyX.Compose(...)` function is the main entry point for rendering the PIN
screen. Embed this in your UI where you want to trigger PIN verification. It provides callbacks for
various authentication outcomes.

```kotlin

@Composable
fun PINKeyX.Compose(
  platformContextProvider: PlatformContextProvider, // Provides platform-specific context
  shouldCheckAvailability: Boolean = true,          // Check for PIN availability/setup
  lockConfig: LockConfig = LockConfig(...),         // Customize PIN
uiTextConfig: PinUiTextConfig = PinUiTextConfig(...), // Customize UI text
onAuthSuccess: () -> Unit,                        // Called on successful PIN auth
onFallback: () -> Unit,                           // Called if user chooses fallback (e.g., Biometric)
onAuthFailure: (String) -> Unit                   // Called on PIN auth failure
){}

```

**Clearing Stored PIN**: The `PINKeyX.clearStore(...)` function allows you to remove any existing PIN data
associated with the application.

```kotlin
fun PINKeyX.clearStore(platformContextProvider: PlatformContextProvider){}
```

### Android App (Jetpack Compose)

In your Android application, you'll need to provide an Android-specific
`PlatformContextProvider`.

```kotlin

@Composable
fun PinProtectedScreen(activity: AppCompatActivity) { // Pass the activity
  var showPinScreen by remember { mutableStateOf(false) }
  var authStatus by remember { mutableStateOf("Awaiting PIN authentication...") }
  val coroutineScope = rememberCoroutineScope()

  val androidPlatformContextProvider = remember { AndroidPlatformContextProvider(activity) }

  Column {
    Button(onClick = { showPinScreen = true }) {
      Text("Enter PIN to Continue")
    }
    Text(text = authStatus)

    Button(onClick = {
      coroutineScope.launch {
        PINKeyX.clearStore(androidPlatformContextProvider)
        authStatus = "Stored PIN cleared (if any)."
      }
    }) {
      Text("Clear Stored PIN")
    }

    if (showPinScreen) {
      PINKeyX.Compose(
        platformContextProvider = androidPlatformContextProvider,
        // Example LockConfig, customize as needed
        lockConfig = LockConfig(pinPromptConfig = PinPromptConfig.default()),
        // Example UiTextConfig, customize as needed
        uiTextConfig = PinUiTextConfig.default().copy(title = "Enter Your Secure PIN"),
        onAuthSuccess = {
          authStatus = "PIN Authentication Successful!"
          showPinScreen = false
          // Navigate to secure content or perform action
        },
        onFallback = {
          authStatus = "User chose a fallback method."
          showPinScreen = false
          // Handle fallback (e.g., show biometric prompt if that's the fallback)
        },
        onAuthFailure = { errorMessage ->
          authStatus = "PIN Authentication Failed: $errorMessage"
          // Optionally keep showPinScreen = true to allow retry, or handle max attempts
        }
      )
    }
  }
}

// In your MainActivity.kt
class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      YourAppTheme {
        PinProtectedScreen(activity = this) // Pass the activity instance
      }
    }
  }
}

```
### Compose Multiplatform App (Shared UI)

For a Compose Multiplatform app,` PINKeyX.Compose` and `PINKeyX.clearStore` are called from your shared
UI code (`commonMain`). You'll rely on `expect/actual` implementations for
`PlatformContextProvider`.Shared Code (`commonMain/kotlin`):

```kotlin

// Expect declaration for getting the platform-specific provider
@Composable
expect fun rememberPlatformContextProvider(): PlatformContextProvider

@Composable
fun PinProtectedScreen() {
  var showPinScreen by remember { mutableStateOf(false) }
  var authStatus by remember { mutableStateOf("Awaiting PIN...") }
  val coroutineScope = rememberCoroutineScope()

  val platformContextProvider = rememberPlatformContextProvider()

  Column {
    Button(onClick = { showPinScreen = true }) {
      Text("Access PIN Protected Feature")
    }
    Text(text = authStatus)

    Button(onClick = {
      coroutineScope.launch {
        PINKeyX.clearStore(platformContextProvider)
        authStatus = "Stored PIN cleared."
      }
    }) {
      Text("Clear App PIN")
    }

    if (showPinScreen) {
      PINKeyX.Compose(
        platformContextProvider = platformContextProvider,
        onAuthSuccess = {
          authStatus = "PIN Accepted!"
          showPinScreen = false
        },
        onFallback = {
          authStatus = "Fallback requested."
          showPinScreen = false
        },
        onAuthFailure = { error ->
          authStatus = "PIN Failed: $error"
        }
      )
    }
  }
}
```

Android Specific (`androidMain/kotlin`) for `rememberPlatformContextProvider`:

```kotlin
@Composable
internal actual fun rememberPlatformContextProvider(): PlatformContextProvider {
  val activity = LocalContext.current as AppCompatActivity // Ensure your current context is an AppCompatActivity
  return remember { AndroidPlatformContextProvider(activity) }
}
```

iOS Specific (`iosMain/kotlin`) for `rememberPlatformContextProvider`:

```kotlin
@Composable
internal actual fun rememberPlatformContextProvider(): PlatformContextProvider {
  return remember { IOSPlatformContextProvider() }
}
```

### iOS App (Conceptual via Compose Multiplatform)

When using Compose Multiplatform for iOS, the `PINKeyX.Compose` and `PINKeyX.clearStore` calls in your
shared UI (`commonMain`) remain the same.


### Additional Configuration

### PIN Code Settings

```kotlin
/**
 * @param lockConfig Configuration for the lock screen behavior, including PIN prompt settings.
 *                   Defaults to a `LockConfig` with default `PinPromptConfig` or you can customize as bellow.
 **/
PinPromptConfig(
  title = "Enter PIN",
  subtitle = "Unlock with your PIN",
  description = "For your security, you need to enter your PIN to continue."
)
/**
 * @param uiTextConfig Configuration for the text displayed in the PIN UI.
 *                     Defaults to `PinUiTextConfig.default()`. or you can customize as bellow
 */
PinUiTextConfig(
  screenTitle = "Enter PIN",
  screenNote = "Please enter your PIN to continue.",
  errorPrefix = "Error: "
)
```

## Changelog

See [CHANGELOG.md](CHANGELOG.md) for a detailed list of changes and version history.

---

Made with ❤️ by [StargateX Mobile](https://github.com/stargatex)
