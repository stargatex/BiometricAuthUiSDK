package com.stargatex.mobile.lib.pinauth

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform