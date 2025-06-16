package com.stargatex.mobile.lib.biometricauth

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform