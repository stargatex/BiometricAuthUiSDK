package com.stargatex.mobile.lib.pinauth.ui.di

import com.stargatex.mobile.lib.pinauth.domain.model.PinMode
import com.stargatex.mobile.lib.pinauth.ui.PINVerifyViewModel
import org.koin.dsl.module

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

internal fun uiModule() = module {
    // Parametrized factory that accepts optional PinMode
    factory { (mode: PinMode?) ->
        PINVerifyViewModel(
            pinUseCase = get(),
            mode = mode
        )
    }
}