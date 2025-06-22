package com.stargatex.mobile.lib.pinauth.ui.model.state

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
sealed interface PinUiEvent {
    data object Success : PinUiEvent
    data class Error(val message: String) : PinUiEvent
}