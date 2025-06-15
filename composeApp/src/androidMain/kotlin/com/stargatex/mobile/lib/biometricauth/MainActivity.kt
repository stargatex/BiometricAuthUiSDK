package com.stargatex.mobile.lib.biometricauth

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stargatex.mobile.lib.biometricauth.di.AndroidPlatformContextProvider
import com.stargatex.mobile.lib.biometricauth.di.FakeAndroidPlatformContextProvider

class MainActivity : AppCompatActivity() {

    private val platformContextProvider = AndroidPlatformContextProvider(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(platformContextProvider)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(FakeAndroidPlatformContextProvider())
}