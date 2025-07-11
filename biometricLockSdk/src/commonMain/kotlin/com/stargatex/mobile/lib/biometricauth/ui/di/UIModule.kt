package com.stargatex.mobile.lib.biometricauth.ui.di

import com.stargatex.mobile.lib.biometricauth.ui.BiometricVerifyViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

internal fun uiModule() = module {
    viewModelOf(::BiometricVerifyViewModel)
}