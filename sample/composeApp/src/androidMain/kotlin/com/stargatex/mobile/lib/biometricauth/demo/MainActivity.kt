package com.stargatex.mobile.lib.biometricauth.demo

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
import com.stargatex.mobile.lib.biometricauth.di.AndroidPlatformContextProvider as BioAndroidPlatformContextProvider
import com.stargatex.mobile.lib.biometricauth.di.FakeAndroidPlatformContextProvider as FakeBioAndroidPlatformContextProvider
import com.stargatex.mobile.lib.pinauth.di.AndroidPlatformContextProvider as PinAndroidPlatformContextProvider
import com.stargatex.mobile.lib.pinauth.di.FakeAndroidPlatformContextProvider as FakePinAndroidPlatformContextProvider

class MainActivity : AppCompatActivity() {

    private val bioPlatformContextProvider = BioAndroidPlatformContextProvider(this)
    private val pinPlatformContextProvider = PinAndroidPlatformContextProvider(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            BiometricWrapperScreen(bioPlatformContextProvider, pinPlatformContextProvider)
        }
    }
}

@Composable
fun BiometricWrapperScreen(
    bioPlatformContextProvider: BioAndroidPlatformContextProvider,
    pinPlatformContextProvider: PinAndroidPlatformContextProvider,
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

    SampleApp(
        bioPlatformContextProvider,
        pinPlatformContextProvider,
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
    SampleApp(FakeBioAndroidPlatformContextProvider()
        , FakePinAndroidPlatformContextProvider())
}