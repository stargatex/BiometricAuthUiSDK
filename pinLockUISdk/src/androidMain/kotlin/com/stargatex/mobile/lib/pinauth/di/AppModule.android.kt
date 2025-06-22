package com.stargatex.mobile.lib.pinauth.di

import androidx.appcompat.app.AppCompatActivity

actual typealias PlatformContext = AppCompatActivity

class AndroidPlatformContextProvider(private val activity: PlatformContext) :
    PlatformContextProvider {
    override fun getPlatformContext(): PlatformContext = activity
}

class FakeAndroidPlatformContextProvider :
    PlatformContextProvider {
    override fun getPlatformContext(): PlatformContext = AppCompatActivity()
}