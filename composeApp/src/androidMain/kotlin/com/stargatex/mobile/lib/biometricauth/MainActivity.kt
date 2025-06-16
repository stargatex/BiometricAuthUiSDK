package com.stargatex.mobile.lib.biometricauth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.stargatex.mobile.lib.biometricauth.di.AndroidPlatformContextProvider
import com.stargatex.mobile.lib.biometricauth.di.FakeAndroidPlatformContextProvider
import com.stargatex.mobile.lib.biometricauth.di.PlatformContextProvider

class MainActivity : AppCompatActivity() {

    private val platformContextProvider = AndroidPlatformContextProvider(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            BiometricWrapperScreen(platformContextProvider)
        }
    }
}

@Composable
fun BiometricWrapperScreen(
    platformContextProvider: PlatformContextProvider,
) {
    var shouldCheck by remember { mutableStateOf(true) }
    var hasLaunchedEnrollment by remember { mutableStateOf(false) }

    val biometricEnrollLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("BiometricWrapperScreen", " Enroll Result $result")
        hasLaunchedEnrollment = false
        //if (result.resultCode == Activity.RESULT_OK) {
        shouldCheck = true
        //}
    }

    App(
        platformContextProvider,
        shouldCheckAvailability = shouldCheck,
        onNoEnrollment = {
            if (!hasLaunchedEnrollment && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                hasLaunchedEnrollment = true
                shouldCheck = false
                val intent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or
                                BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    )
                }
                biometricEnrollLauncher.launch(intent)
            }
        }
    )
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(FakeAndroidPlatformContextProvider())
}