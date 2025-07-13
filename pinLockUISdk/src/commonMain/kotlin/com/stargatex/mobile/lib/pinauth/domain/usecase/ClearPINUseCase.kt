package com.stargatex.mobile.lib.pinauth.domain.usecase

import com.stargatex.mobile.lib.pinauth.platform.store.pref.PrefStore

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
internal class ClearPINUseCase(
    private val prefStore: PrefStore
) {
    operator fun invoke() {
        prefStore.clearAll()
    }
}