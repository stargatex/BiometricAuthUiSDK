package com.stargatex.mobile.lib.pinauth.di

import androidx.appcompat.app.AppCompatActivity

internal actual typealias PlatformContext = AppCompatActivity

public class AndroidPlatformContextProvider(private val activity: PlatformContext) :
    PlatformContextProvider {
    override fun getPlatformContext(): PlatformContext = activity
}

public class FakeAndroidPlatformContextProvider :
    PlatformContextProvider {
    override fun getPlatformContext(): PlatformContext = AppCompatActivity()
}