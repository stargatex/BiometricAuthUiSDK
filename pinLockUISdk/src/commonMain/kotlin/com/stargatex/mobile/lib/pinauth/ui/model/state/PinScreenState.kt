package com.stargatex.mobile.lib.pinauth.ui.model.state

/**
 * Sealed interface representing the various states of the PIN screen.
 * Each state represents a different stage in the PIN authentication or setup flow.
 *
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
internal sealed interface PinScreenState {
    /** Initial loading state before determining the actual PIN operation. */
    data object Loading : PinScreenState

    /** State for setting a new PIN (first entry). Part of the SET and CHANGE flows. */
    data object Save : PinScreenState

    /** State for confirming a new PIN (second entry). Part of the SET and CHANGE flows. */
    data object Confirm : PinScreenState

    /** State for unlocking/verifying an existing PIN. Used in UNLOCK mode. */
    data object Unlock : PinScreenState

    /** State for verifying the old PIN before changing. First step in CHANGE mode. */
    data object VerifyOldForChange : PinScreenState

    /** State for setting a new PIN after old PIN verification. Part of CHANGE flow. */
    data object SaveNew : PinScreenState

    /** State for confirming the new PIN after old PIN verification. Part of CHANGE flow. */
    data object ConfirmNew : PinScreenState
}