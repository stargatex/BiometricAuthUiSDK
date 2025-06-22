package com.stargatex.mobile.lib.pinauth.domain.usecase

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
data class PINUseCase(
    val savedPINUseCase: SavePINUseCase,
    val fetchSavedPINUseCase: FetchSavedPINUseCase
)
