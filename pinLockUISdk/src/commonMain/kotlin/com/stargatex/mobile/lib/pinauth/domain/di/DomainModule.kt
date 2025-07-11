package com.stargatex.mobile.lib.pinauth.domain.di

import com.stargatex.mobile.lib.pinauth.domain.usecase.FetchSavedPINUseCase
import com.stargatex.mobile.lib.pinauth.domain.usecase.PINUseCase
import com.stargatex.mobile.lib.pinauth.domain.usecase.SavePINUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

internal fun domainModule() = module {
    factoryOf(::PINUseCase)
    factoryOf(::SavePINUseCase)
    factoryOf(::FetchSavedPINUseCase)
}