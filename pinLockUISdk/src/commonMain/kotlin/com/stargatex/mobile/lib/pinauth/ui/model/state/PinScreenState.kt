package com.stargatex.mobile.lib.pinauth.ui.model.state

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
internal sealed interface PinScreenState {
    data object Loading : PinScreenState
    data object Save : PinScreenState
    data object Confirm : PinScreenState
    data object Unlock : PinScreenState
}