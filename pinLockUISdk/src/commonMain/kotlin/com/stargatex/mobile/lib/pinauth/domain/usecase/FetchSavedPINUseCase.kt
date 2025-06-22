package com.stargatex.mobile.lib.pinauth.domain.usecase

import com.stargatex.mobile.lib.pinauth.platform.store.pref.PrefStore
import com.stargatex.mobile.lib.pinauth.platform.store.quilifier.LibStoreConstant

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
class FetchSavedPINUseCase(
    private val prefStore: PrefStore
) {

    suspend operator fun invoke(): String? {
        return prefStore.getString(LibStoreConstant.KEY_PIN_CODE)
    }
}