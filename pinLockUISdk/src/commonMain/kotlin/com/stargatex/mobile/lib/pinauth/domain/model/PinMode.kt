package com.stargatex.mobile.lib.pinauth.domain.model

/**
 * Enum representing the different modes of PIN operation.
 *
 * @property UNLOCK Verify the existing PIN to unlock/authenticate.
 * @property SET Set a new PIN for the first time (no existing PIN required).
 * @property CHANGE Change the existing PIN (verify old PIN, then set new one).
 *
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
public enum class PinMode {
    /**
     * Unlock mode: User enters their existing PIN to verify/authenticate.
     * Used when a PIN is already set on the device.
     */
    UNLOCK,

    /**
     * Set mode: User creates a new PIN for the first time.
     * Used when no PIN is currently set on the device.
     */
    SET,

    /**
     * Change mode: User verifies their existing PIN, then creates a new one.
     * Automatically handles the two-step verification + new PIN setup process.
     * The library manages all state transitions internally.
     */
    CHANGE
}

