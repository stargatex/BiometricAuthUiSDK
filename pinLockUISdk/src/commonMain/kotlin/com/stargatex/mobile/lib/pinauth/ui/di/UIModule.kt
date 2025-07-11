package com.stargatex.mobile.lib.pinauth.ui.di

import com.stargatex.mobile.lib.pinauth.ui.PINVerifyViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

internal fun uiModule() = module {
    viewModelOf(::PINVerifyViewModel)
}